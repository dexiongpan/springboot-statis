package com.statis.statis.activemq.polling.producer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.activemq.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.jms.UncategorizedJmsException;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class PollingTemplateProxyFactoryBean implements FactoryBean,Service{
	private static final Logger logger = LoggerFactory.getLogger(PollingTemplateProxyFactoryBean.class);

	private PollingTemplate pollingTemplate = null;
	
	private int tryTimes = 1;
	private final Map<Integer, JmsTemplate> UnavailableBrokers = new ConcurrentHashMap<Integer, JmsTemplate>();
	public Timer timer = new Timer();
	private long reConnectTime = 10000;
	public long getReConnectTime() {
		return reConnectTime;
	}
	public void setReConnectTime(long reConnectTime) {
		this.reConnectTime = reConnectTime;
	}
	
	private long slowlyTime = 2;
	
	public long getSlowlyTime() {
		return slowlyTime;
	}

	public void setSlowlyTime(long slowlyTime) {
		this.slowlyTime = slowlyTime;
	}
	
	private int slowCount = 3;

	public int getSlowCount() {
		return slowCount;
	}

	public void setSlowCount(int slowCount) {
		this.slowCount = slowCount;
	}
	
	private long slowBrokerBlockTime = 100;

	public long getSlowBrokerBlockTime() {
		return slowBrokerBlockTime;
	}

	public void setSlowBrokerBlockTime(long slowBrokerBlockTime) {
		this.slowBrokerBlockTime = slowBrokerBlockTime;
	}
	
	private final Map<String, AtomicInteger> slowCountMap = new ConcurrentHashMap<String, AtomicInteger>();

	private final Map<Integer, JmsTemplate> slowBrokers = new ConcurrentHashMap<Integer, JmsTemplate>();

	private List<JmsTemplate> allTemplate;
	
	
	class RevertTast extends TimerTask {

		@Override
		public void run() {
			logger.info("RevertTask timer running");
			for (Integer i : UnavailableBrokers.keySet()) {
				JmsTemplate temp = UnavailableBrokers.remove(i);
				if (temp != null) {
					pollingTemplate.getJmsTemplateList().set(i, temp);
					logger.info("JmsTemplate [{}] put back to pollingTemplate's jmsTemplateList", i);
				} else {
					logger.warn(" UnavailableBrokers key [{}] value is null, maybe is removed", i);
				}
			}
		}
	}
	
	class RevertSlowBrokerTask extends TimerTask {
		int index;

		RevertSlowBrokerTask(int i) {
			this.index = i;
		}

		@Override
		public void run() {
			logger.info("RevertSlowBrokerTask timer running");
			JmsTemplate temp = slowBrokers.remove(this.index);
			if (temp != null) {
				pollingTemplate.getJmsTemplateList().set(index, temp);
				logger.info("In RevertSlowBrokerTask, JmsTemplate [{}] put back to pollingTemplate's list", index);
			}
		}
	}
	
	
	class ProxyHandler implements InvocationHandler {

		private final Logger log = LoggerFactory.getLogger(ProxyHandler.class);

		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			Object object = null;

			long time = 0L;

			boolean sucess = false;
			for (int t = 0; t < tryTimes; t++) {
				try {
					time = System.currentTimeMillis();

					object = method.invoke(pollingTemplate, args);
					sucess = true;

					time = System.currentTimeMillis() - time;

					break;
				} catch (Exception e) {
					log.warn("Jms Operation {} failed, at {}; the exception message : {}", method.getName(), pollingTemplate.getLastOperationDistinationBrokerUrl(), e.getCause().getMessage());
					excludeBroker();
				}
			}

			/**
			 * 检查执行所花的时间，如果耗费的时间过长，则将该template放入到slowBrokersMap中，暂时屏蔽掉。
			 */
			if (sucess == true) {
				AtomicInteger c = slowCountMap.get(pollingTemplate.getLastlyBrokerUrl());
				if (c == null) {
					c = new AtomicInteger();
					slowCountMap.put(pollingTemplate.getLastlyBrokerUrl(), c);
				}
				if (time > slowlyTime) {
					log.warn("the broker [{}] is slowly, spend time is {} millisecond, already had times [{}]", pollingTemplate.getLastlyBrokerUrl(), time, c.get());
					if (c.incrementAndGet() >= slowCount) {
						log.warn("the broker [{}] is slowly, spend time is {} millisecond, it will be exclude, totle [{}] times greater than slowlycount {}", pollingTemplate.getLastlyBrokerUrl(),
								time, c.get(), slowCount);
						c.set(0);
						excludeSlowBroker();
					}
				} else {
					c.set(0);
				}
			}
			/**
			 * 如果 JmsTemplatelist中無可用的JmsTemplate,
			 * 则先尝试用slowBroker中的JmsTemplates进行发送。
			 */
			if (sucess == false) {
				log.warn("try to use slowbrokers jmsTemplates");
				for (Entry<Integer, JmsTemplate> en : slowBrokers.entrySet()) {
					JmsTemplate template = en.getValue();
					if (template != null) {
						try {
							object = method.invoke(template, args);
							sucess = true;
							break;
						} catch (Exception e) {
							log.debug("In slowBrokers operation faild, at [{}] in JmsTemplateList.", en.getKey(), e);
						}
					}
				}
			}

			/**
			 * 如果前面的操作都失败了，则尝试使用UnavailableBrokers中的JmsTemplate
			 */
			if (sucess == false) {
				log.warn("try to use temporary unavailabe jmsTemplates");
				for (Entry<Integer, JmsTemplate> en : UnavailableBrokers.entrySet()) {
					JmsTemplate template = en.getValue();
					if (template != null) {
						try {
							object = method.invoke(template, args);
							sucess = true;
							break;
						} catch (Exception e) {
							log.debug("In unavailable brokers,  operation faild at [{}] in JmsTemplateList.", en.getKey(), e);
						}
					}
				}
			}

			/**
			 * 如果前面的错误都失败，则尝试对所有的JmsTemplate进行操作
			 */
			if (sucess == false) {
				logger.warn("try to use template in allTemplateMap");
				for (Entry<Integer, JmsTemplate> en : pollingTemplate.getAllTemplateMap().entrySet()) {
					JmsTemplate template = en.getValue();
					if (template != null) {
						try {
							object = method.invoke(template, args);
							sucess = true;
							break;
						} catch (Exception e) {
							log.debug("In allTemplateMap,  operation faild at [{}] in JmsTemplateList.", en.getKey(), e);
						}
					}
				}
			}

			if (sucess) {
				return object;
			} else {
				log.error("all brokers are tryed to opertaion {}, but still failed", method.getName());
				throw new UncategorizedJmsException("Maybe all brokers is unaviliable at this time. the method [ " + method.getName() + "] failed");
			}
		}
	}
	
	private void excludeBroker() {
		int k = pollingTemplate.getLastlyBrokerIndex();
		if (k >= 0) {
			// 多线程执行时，
			// issueTemplate=pollingTemplate.getJmsTemplateList().get(k)
			// 可能为空值，放到ConcurentHashMap.put方法的value不能为空，否则抛出空指针异常
			JmsTemplate issueTemplate = pollingTemplate.getJmsTemplateList().get(k);
			if (issueTemplate != null) {
				logger.warn("the issueTemplate is not null at [{}] in jmsTemplatelist , it will put to UnavailableBrokersMap", k);
				pollingTemplate.getJmsTemplateList().set(k, null);
				// ConcurreyHashMap的value值不能为空，否则会抛出空指针异常。
				UnavailableBrokers.put(k, issueTemplate);
				timer.schedule(new RevertTast(), reConnectTime);
				logger.warn("add RevertTask to timer with the issue [{}] jmsTemplate, broker Url [{}]", k, pollingTemplate.getLastlyBrokerUrl());
			} else {
				logger.debug("the issueTemplate is null at [{}] in jmsTemplatelist, don't need to put it to UnavailableBrokersMap", k);
			}
		}
	}

	/**
	 * 暂时排除掉处理速度慢的broker的JmsTemplate
	 */
	private void excludeSlowBroker() {
		int k = pollingTemplate.getLastlyBrokerIndex();
		if (k >= 0) {
			// 多线程执行时，
			// issueTemplate=pollingTemplate.getJmsTemplateList().get(k)
			// 可能为空值，放到ConcurentHashMap.put方法的value不能为空，否则抛出空指针异常
			JmsTemplate slowTemplate = pollingTemplate.getJmsTemplateList().get(k);
			if (slowTemplate != null) {
				logger.warn("put the slowTemplate [{}] into SlowBrokersMap ", k);
				pollingTemplate.getJmsTemplateList().set(k, null);
				// ConcurreyHashMap的value值不能为空，否则会抛出空指针异常。
				slowBrokers.put(k, slowTemplate);
				timer.schedule(new RevertSlowBrokerTask(k), slowBrokerBlockTime);
				logger.warn("add RevertSlowBrokerTask to timer with the slowBroker [{}] jmsTemplate, broker Url [{}]", k, pollingTemplate.getLastlyBrokerUrl());
			} else {
				logger.debug("the slowTemplate is null at [{}] in jmsTemplatelist, don't need to put it to slowBrokerMap", k);
			}
		}
	}
	
	@Override
	public Object getObject() throws Exception {
		assert (pollingTemplate != null);
		tryTimes = pollingTemplate.getBrokerURLs().size();
		assert (tryTimes > 0);
		logger.debug("to create proxy object that implements JmsOperations.class");
		return Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] { JmsOperations.class }, new ProxyHandler());
	}
	@Override
	public void start() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
		timer.cancel();
	}

	

    
	public PollingTemplate getPollingTemplate() {
		return pollingTemplate;
	}
	public void setPollingTemplate(PollingTemplate pollingTemplate) {
		this.pollingTemplate = pollingTemplate;
	}
	@Override
	public Class getObjectType() {
		// TODO Auto-generated method stub
		return JmsOperations.class;
	}
    
	public boolean isSingleton() {
		return false;
	}
}
