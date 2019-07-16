package com.statis.statis.activemq.polling.producer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.concurrent.atomic.AtomicLong;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.Queue;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.Service;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.JmsException;
import org.springframework.jms.UncategorizedJmsException;
import org.springframework.jms.core.BrowserCallback;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.jms.core.ProducerCallback;
import org.springframework.jms.core.SessionCallback;
import org.springframework.stereotype.Component;

import com.statis.statis.activemq.ActiveMQQueueConst;

@Component
public class PollingTemplate implements Service,JmsOperations{
    private static final Logger logger= LoggerFactory.getLogger(PollingTemplate.class);
    
    private List<String> brokerURLs = ActiveMQQueueConst.brokerURLs();
    
    private int maxConnections = 1;
    
    //private String defaultQueueName = "Test";
    
    private String defaultQueueName = ActiveMQQueueConst.QUEUE_NAME_VERIFY;
    
    protected int brokerNum = 0;
    
    private AtomicLong count = new AtomicLong();
    
    private ActiveMQQueue defaultActiveMQQueue = null;
    
    protected List<JmsTemplate> jmsTemplateList = null;
    
    protected final Map<Integer, JmsTemplate> allTemplateMap = new HashMap<Integer, JmsTemplate>();
    
    private List<PooledConnectionFactory> pooledConnectionFactoryList = null;
    
    private ThreadLocal<String> distinationBrokerUrl =  new ThreadLocal<String>();
    
    private ThreadLocal<Integer> distinationBrokerIndex = new ThreadLocal<Integer>() {
    		public Integer initialValue() {
    			return new Integer(-1);
    		};
      };
    
  	public String getLastlyBrokerUrl() {
		return distinationBrokerUrl.get();
	}

	public int getLastlyBrokerIndex() {
		return distinationBrokerIndex.get();
	}
      
  	@Override
  	public void start() throws Exception {
  		// TODO Auto-generated method stub
  		logger.info("start PollingTemplate");
  		assert (brokerURLs != null);
  		assert (brokerURLs.size() > 0);
  		assert (defaultQueueName != null && defaultQueueName.length() > 0);
  		
  		brokerNum = brokerURLs.size();
  		defaultActiveMQQueue = new ActiveMQQueue(defaultQueueName);
  		pooledConnectionFactoryList = new ArrayList<PooledConnectionFactory>();
  		jmsTemplateList = new ArrayList<JmsTemplate>();
  		
  		for(int index =0; index< brokerNum; index++) {
  			ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
  			activeMQConnectionFactory.setTrustAllPackages(true);
  			activeMQConnectionFactory.setBrokerURL(brokerURLs.get(index));
  			PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
  			pooledConnectionFactory.setConnectionFactory(activeMQConnectionFactory);
  			pooledConnectionFactory.setMaxConnections(maxConnections);
  			
  			JmsTemplate jmsTemplate = new JmsTemplate();
  			jmsTemplate.setConnectionFactory(pooledConnectionFactory);
  			jmsTemplate.setDefaultDestination(defaultActiveMQQueue);
  			
  			
  			pooledConnectionFactoryList.add(pooledConnectionFactory);
  			jmsTemplateList.add(jmsTemplate);
  			
  			allTemplateMap.put(index, jmsTemplate);
  			
  			/**
  			 * 启动时验证broker Url是否可用
  			 * 
  			 * */
  			try {
  				logger.info("connect to broker url [{}]",brokerURLs.get(index));
  	  			Connection con = pooledConnectionFactory.createConnection();
  	  			con.start();
  	  			con.close();
  	  			logger.debug("broker [{}] pooledConnectionFactory has [{}] Connections in pool",brokerURLs.get(index),pooledConnectionFactory.getNumConnections());
			} catch (Exception e) {
				// TODO: handle exception
				logger.error("connect to broker failed, broker url [{}]",brokerURLs.get(index));
				logger.debug("connect to broker failed, broker url [{}]",brokerURLs.get(index),e);
			}
  		}
  		logger.debug("JmsTemplateList has {} element",jmsTemplateList.size());
  	}
    
	@Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
		logger.info("stop PollingTemplate");
		for(PooledConnectionFactory item:pooledConnectionFactoryList) {
			item.stop();
		}
	}
	
	protected JmsTemplate pickJmsTemplate () {
		long c = count.getAndIncrement();
		JmsTemplate template = null;
		for(long j =c;j< c+brokerNum;j++) {
			int index = (int)j%brokerNum;
			template = jmsTemplateList.get(index);
			if(template != null) {
				distinationBrokerUrl.set(brokerURLs.get(index));
				distinationBrokerIndex.set(index);
				break;
			}
		}
		if(template == null) {
			distinationBrokerUrl.set(null);
			distinationBrokerIndex.set(-1);
			throw new UncategorizedJmsException("JmsTemplateList's items are null, maybe the jmstemplates are not avaliabe for the monment");
		}
		return template;
	}
	
	public String getLastOperationDistinationBrokerUrl() {
		return distinationBrokerUrl.get();
	}
	
	
	@Override
	public <T> T execute(SessionCallback<T> action) throws JmsException {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> getBrokerURLs() {
		return brokerURLs;
	}

	public void setBrokerURLs(List<String> brokerURLs) {
		this.brokerURLs = brokerURLs;
	}

	public int getMaxConnections() {
		return maxConnections;
	}

	public void setMaxConnections(int maxConnections) {
		this.maxConnections = maxConnections;
	}

	public String getDefaultQueueName() {
		return defaultQueueName;
	}

	public void setDefaultQueueName(String defaultQueueName) {
		this.defaultQueueName = defaultQueueName;
	}

	public int getBrokerNum() {
		return brokerNum;
	}

	public void setBrokerNum(int brokerNum) {
		this.brokerNum = brokerNum;
	}

	public AtomicLong getCount() {
		return count;
	}

	public void setCount(AtomicLong count) {
		this.count = count;
	}

	public ActiveMQQueue getDefaultActiveMQQueue() {
		return defaultActiveMQQueue;
	}

	public void setDefaultActiveMQQueue(ActiveMQQueue defaultActiveMQQueue) {
		this.defaultActiveMQQueue = defaultActiveMQQueue;
	}

	public List<JmsTemplate> getJmsTemplateList() {
		return jmsTemplateList;
	}

	public void setJmsTemplateList(List<JmsTemplate> jmsTemplateList) {
		this.jmsTemplateList = jmsTemplateList;
	}

	public ThreadLocal<String> getDistinationBrokerUrl() {
		return distinationBrokerUrl;
	}

	public void setDistinationBrokerUrl(ThreadLocal<String> distinationBrokerUrl) {
		this.distinationBrokerUrl = distinationBrokerUrl;
	}

	public ThreadLocal<Integer> getDistinationBrokerIndex() {
		return distinationBrokerIndex;
	}

	public void setDistinationBrokerIndex(ThreadLocal<Integer> distinationBrokerIndex) {
		this.distinationBrokerIndex = distinationBrokerIndex;
	}

	public Map<Integer, JmsTemplate> getAllTemplateMap() {
		return allTemplateMap;
	}

	@Override
	public <T> T execute(ProducerCallback<T> action) throws JmsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T execute(Destination destination, ProducerCallback<T> action) throws JmsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T execute(String destinationName, ProducerCallback<T> action) throws JmsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void send(MessageCreator messageCreator) throws JmsException {
		// TODO Auto-generated method stub
		this.pickJmsTemplate().send(messageCreator);
	}

	@Override
	public void send(Destination destination, MessageCreator messageCreator) throws JmsException {
		// TODO Auto-generated method stub
		this.pickJmsTemplate().send(destination, messageCreator);
	}

	@Override
	public void send(String destinationName, MessageCreator messageCreator) throws JmsException {
		// TODO Auto-generated method stub
		this.pickJmsTemplate().send(destinationName, messageCreator);
	}

	@Override
	public void convertAndSend(Object message) throws JmsException {
		// TODO Auto-generated method stub
		this.pickJmsTemplate().convertAndSend(message);
	}

	@Override
	public void convertAndSend(Destination destination, Object message) throws JmsException {
		// TODO Auto-generated method stub
		this.pickJmsTemplate().convertAndSend(destination, message);
	}

	@Override
	public void convertAndSend(String destinationName, Object message) throws JmsException {
		// TODO Auto-generated method stub
		this.pickJmsTemplate().convertAndSend(destinationName, message);
	}

	@Override
	public void convertAndSend(Object message, MessagePostProcessor postProcessor) throws JmsException {
		// TODO Auto-generated method stub
		this.pickJmsTemplate().convertAndSend(message, postProcessor);
	}

	@Override
	public void convertAndSend(Destination destination, Object message, MessagePostProcessor postProcessor)
			throws JmsException {
		// TODO Auto-generated method stub
		this.pickJmsTemplate().convertAndSend(destination, message,
				postProcessor);
	}

	@Override
	public void convertAndSend(String destinationName, Object message, MessagePostProcessor postProcessor)
			throws JmsException {
		// TODO Auto-generated method stub
		this.pickJmsTemplate().convertAndSend(destinationName, message,
				postProcessor);
	}

	@Override
	public Message receive() throws JmsException {
		// TODO Auto-generated method stub
		return this.pickJmsTemplate().receive();
	}

	@Override
	public Message receive(Destination destination) throws JmsException {
		// TODO Auto-generated method stub
		return this.pickJmsTemplate().receive(destination);
	}

	@Override
	public Message receive(String destinationName) throws JmsException {
		// TODO Auto-generated method stub
		return this.pickJmsTemplate().receive(destinationName);
	}

	@Override
	public Message receiveSelected(String messageSelector) throws JmsException {
		// TODO Auto-generated method stub
		return this.pickJmsTemplate().receiveSelected(messageSelector);
	}

	@Override
	public Message receiveSelected(Destination destination, String messageSelector) throws JmsException {
		// TODO Auto-generated method stub
		return this.pickJmsTemplate().receiveSelected(destination,
				messageSelector);
	}

	@Override
	public Message receiveSelected(String destinationName, String messageSelector) throws JmsException {
		// TODO Auto-generated method stub
		return this.pickJmsTemplate().receiveSelected(destinationName,
				messageSelector);
	}

	@Override
	public Object receiveAndConvert() throws JmsException {
		// TODO Auto-generated method stub
		return this.pickJmsTemplate().receiveAndConvert();
	}

	@Override
	public Object receiveAndConvert(Destination destination) throws JmsException {
		// TODO Auto-generated method stub
		return this.pickJmsTemplate().receiveAndConvert(destination);
	}

	@Override
	public Object receiveAndConvert(String destinationName) throws JmsException {
		// TODO Auto-generated method stub
		return this.pickJmsTemplate().receiveAndConvert(destinationName);
	}

	@Override
	public Object receiveSelectedAndConvert(String messageSelector) throws JmsException {
		// TODO Auto-generated method stub
		return this.pickJmsTemplate()
				.receiveSelectedAndConvert(messageSelector);
	}

	@Override
	public Object receiveSelectedAndConvert(Destination destination, String messageSelector) throws JmsException {
		// TODO Auto-generated method stub
		return this.pickJmsTemplate().receiveSelectedAndConvert(destination,
				messageSelector);
	}

	@Override
	public Object receiveSelectedAndConvert(String destinationName, String messageSelector) throws JmsException {
		// TODO Auto-generated method stub
		return this.pickJmsTemplate().receiveSelectedAndConvert(
				destinationName, messageSelector);
	}

	@Override
	public Message sendAndReceive(MessageCreator messageCreator) throws JmsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Message sendAndReceive(Destination destination, MessageCreator messageCreator) throws JmsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Message sendAndReceive(String destinationName, MessageCreator messageCreator) throws JmsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T browse(BrowserCallback<T> action) throws JmsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T browse(Queue queue, BrowserCallback<T> action) throws JmsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T browse(String queueName, BrowserCallback<T> action) throws JmsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T browseSelected(String messageSelector, BrowserCallback<T> action) throws JmsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T browseSelected(Queue queue, String messageSelector, BrowserCallback<T> action) throws JmsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T browseSelected(String queueName, String messageSelector, BrowserCallback<T> action)
			throws JmsException {
		// TODO Auto-generated method stub
		return null;
	}





}
