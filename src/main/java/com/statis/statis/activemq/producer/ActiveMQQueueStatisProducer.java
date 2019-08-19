package com.statis.statis.activemq.producer;

import javax.jms.Destination;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import com.statis.statis.activemq.DestinationFactory;
import com.statis.statis.model.ContentCount;

@Component
public class ActiveMQQueueStatisProducer {
  
	private final static Logger logger = LoggerFactory.getLogger(ActiveMQQueueStatisProducer.class);
	
	@Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;
	
	 public void sendMsgData(String destinationName,ContentCount message)  {
		 Destination destination = DestinationFactory.getDestination(destinationName);
	    	jmsMessagingTemplate.convertAndSend(destination, message);
	 }
}
