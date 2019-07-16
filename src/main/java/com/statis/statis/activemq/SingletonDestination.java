package com.statis.statis.activemq;

import javax.jms.Destination;

import org.apache.activemq.command.ActiveMQQueue;

public class SingletonDestination {
	private volatile static Destination requestDestination;
	private volatile static Destination verifyDestination;
	private volatile static Destination dbDestination;
    public static Destination getRequestDestination() {  
    if (requestDestination == null) {  
        synchronized (Destination.class) {  
        if (requestDestination == null) {  
        	requestDestination = new ActiveMQQueue(ActiveMQQueueConst.QUEUE_NAME_REQUEST_TEST);  
        }  
        }  
    }  
    return requestDestination;  
    }
    
    public static Destination getDbDestination() {  
    if (dbDestination == null) {  
        synchronized (Destination.class) {  
        if (dbDestination == null) {  
        	dbDestination = new ActiveMQQueue(ActiveMQQueueConst.QUEUE_NAME_DBOPERATE);  
        }  
        }  
    }  
    return dbDestination;  
    }
    
    public static Destination getVerifyDestination() {  
    if (verifyDestination == null) {  
        synchronized (Destination.class) {  
        if (verifyDestination == null) {  
        	verifyDestination = new ActiveMQQueue(ActiveMQQueueConst.QUEUE_NAME_VERIFY);  
        }  
        }  
    }  
    return verifyDestination;  
    }
    
}
