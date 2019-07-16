package com.statis.statis.activemq;

import javax.jms.Destination;

public class DestinationFactory {
	
	public static Destination getDestination(String destinationName) {  
        if (ActiveMQQueueConst.QUEUE_NAME_REQUEST_TEST.equals(destinationName)) {  
            return SingletonDestination.getRequestDestination();  
        } else if (ActiveMQQueueConst.QUEUE_NAME_VERIFY.equals(destinationName)) {  
            return SingletonDestination.getVerifyDestination();  
        }else if(ActiveMQQueueConst.QUEUE_NAME_DBOPERATE.equals(destinationName)) {
        	return SingletonDestination.getDbDestination();
        }
        return null;
    }  

}
