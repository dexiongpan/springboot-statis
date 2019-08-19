//package com.statis.statis.activemq.polling.producer;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.jms.JMSException;
//import javax.jms.Message;
//import javax.jms.Session;
//import javax.jms.TextMessage;
//
//import org.apache.activemq.Service;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jms.JmsException;
//import org.springframework.jms.core.JmsOperations;
//import org.springframework.jms.core.MessageCreator;
//import org.springframework.stereotype.Component;
//
//import com.statis.statis.activemq.ActiveMQQueueConst;
//
//@Component
//public class GroupQueueSender implements Service{
//	private static final Logger logger = LoggerFactory
//			.getLogger(GroupQueueSender.class);
//	//private String groupQueueName = "TestGroup";
//	private String groupQueueName = ActiveMQQueueConst.QUEUE_NAME_VERIFY;
//	
//	//private PollingTemplate pollingTemplate = new PollingTemplate();
//	@Autowired
//	private PollingTemplate pollingTemplate;
//	
//	@Autowired
//	private PollingTemplateProxyFactoryBean pollingTemplateProxyFactoryBean;
//	//private PollingTemplateProxyFactoryBean pollingTemplateProxyFactoryBean = new PollingTemplateProxyFactoryBean();
//	
//	private JmsOperations jmsOperations = null;
//	
//	private String jointMark = "-";
//
//	public long getSlowlyTime() {
//		return this.pollingTemplateProxyFactoryBean.getSlowlyTime();
//	}
//	
//	public void setSlowlyTime(long slowlyTime) {
//		this.pollingTemplateProxyFactoryBean.setSlowlyTime(slowlyTime);
//	}
//
//	public int getSlowCount() {
//		return this.pollingTemplateProxyFactoryBean.getSlowCount();
//	}
//	
//	public void setSlowCount(int slowCount) {
//		this.pollingTemplateProxyFactoryBean.setSlowCount(slowCount);
//	}
//
//	public String getGroupQueueName() {
//		return groupQueueName;
//	}
//	
//	public long getSlowBrokerBlockTime() {
//		return pollingTemplateProxyFactoryBean.getSlowBrokerBlockTime();
//	}
//	
//	public void setSlowBrokerBlockTime(long slowBrokerBlockTime) {
//		this.pollingTemplateProxyFactoryBean.setSlowBrokerBlockTime(slowBrokerBlockTime);
//	}
//	
//	
//
//	public void setGroupQueueName(String groupQueueName) {
//		this.groupQueueName = groupQueueName;
//	}
//
//	public void setBrokerURLs(List<String> brokerURLs) {
//		this.pollingTemplate.setBrokerURLs(brokerURLs);
//	}
//
//	public List<String> getBrokerURLs() {
//		return pollingTemplate.getBrokerURLs();
//	}
//
//	public int getMaxConnections() {
//		return pollingTemplate.getMaxConnections();
//	}
//
//	public void setMaxConnections(int maxConnections) {
//		this.pollingTemplate.setMaxConnections(maxConnections);
//	}
//	@Override
//	public void start() throws Exception {
//		// TODO Auto-generated method stub
//		logger.info("start GroupQueueSender");
//
//		pollingTemplate.setDefaultQueueName(groupQueueName + jointMark
//				+ "default");
//
//		pollingTemplate.start();
//
//		pollingTemplateProxyFactoryBean.setPollingTemplate(pollingTemplate);
//		jmsOperations = (JmsOperations) pollingTemplateProxyFactoryBean
//				.getObject();
//	}
//
//	@Override
//	public void stop() throws Exception {
//		// TODO Auto-generated method stub
//		this.pollingTemplate.stop();
//		pollingTemplateProxyFactoryBean.stop();
//	}
//    
//	
//	public void send(MessageCreator messageCreator) throws JmsException {
//		jmsOperations.send(messageCreator);
//	}
//
//	public void send(String subName, MessageCreator messageCreator)
//			throws JmsException {
//		jmsOperations
//				.send(groupQueueName + jointMark + subName, messageCreator);
//	}
//	
//	public void sendMsg(final String message, String subQueueName,
//			String systemId) throws JmsException {
//
//		logger.debug("sendmessage:{}", message);
//		final Map<String, String> stringProperty = new HashMap<String, String>();
//		if (systemId != null && systemId.length() > 0) {
//			stringProperty.put("systemcode", systemId);
//		}
//
//		MessageCreator messageCreator = new MessageCreator() {
//			public Message createMessage(Session session) throws JMSException {
//				TextMessage txtmsg = session.createTextMessage();
//				txtmsg.setText(message);
//				if (stringProperty != null && stringProperty.size() > 0) {
//					for (String key : stringProperty.keySet()) {
//						txtmsg.setStringProperty(key, stringProperty.get(key));
//					}
//				}
//				return txtmsg;
//			}
//		};
//
//		if (subQueueName != null && subQueueName.length() > 0) {
//			this.send(subQueueName, messageCreator);
//		} else {
//			this.send(messageCreator);
//		}
//	}
//	
//
//	public void sendMsg(String message, String subQueueName) {
//		this.sendMsg(message, subQueueName, null);
//	}
//	
//	public void sendMsg(Object message) {
//		jmsOperations.convertAndSend(message);
//	}
//}
