package com.statis.statis.activemq;

import javax.jms.ConnectionFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.SimpleJmsListenerContainerFactory;

@Configuration
public class ActiveMQConfiguration {
   
	  @Bean(ActiveMQQueueConst.BEAN_NAME_JMSLISTENERCONTAINERFACTORY)
	    public JmsListenerContainerFactory<?> queueJmsListenerContainerFactory(ConnectionFactory connectionFactory){
	        SimpleJmsListenerContainerFactory factory = new SimpleJmsListenerContainerFactory();
	        factory.setConnectionFactory(connectionFactory);
	        factory.setPubSubDomain(false);
	        return factory;
	    }
}
