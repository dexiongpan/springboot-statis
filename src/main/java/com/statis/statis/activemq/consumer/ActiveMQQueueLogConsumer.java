package com.statis.statis.activemq.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.statis.statis.activemq.ActiveMQQueueConst;
import com.statis.statis.vo.MessageData;

@Component
public class ActiveMQQueueLogConsumer {

	private final static Logger logger = LoggerFactory.getLogger(ActiveMQQueueLogConsumer.class);
	
	   @JmsListener(destination = ActiveMQQueueConst.QUEUE_NAME_EXTRACT_LOG, containerFactory = ActiveMQQueueConst.BEAN_NAME_JMSLISTENERCONTAINERFACTORY)
	    public void receiveQueueLogMsg(String message) {
		   System.out.println(message);
	   }
}
