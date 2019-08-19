package com.statis.statis.activemq;

import javax.jms.Destination;

import org.apache.activemq.command.ActiveMQQueue;

public class SingletonDestination {
	private volatile static Destination requestDestination;
	private volatile static Destination verifyDestination;
	private volatile static Destination dbDestination;
	private volatile static Destination statisContentDestination; 
	private volatile static Destination extractLogDestination; 
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
    
    public static Destination getContentDestination() {  
    if (statisContentDestination == null) {  
        synchronized (Destination.class) {  
        if (statisContentDestination == null) {  
        	statisContentDestination = new ActiveMQQueue(ActiveMQQueueConst.QUEUE_NAME_STATIS_COTENT);  
        }  
        }  
    }  
    return statisContentDestination;  
    }
    
    public static Destination getExtractLogDestination() {  
    if (extractLogDestination == null) {  
        synchronized (Destination.class) {  
        if (extractLogDestination == null) {  
        	extractLogDestination = new ActiveMQQueue(ActiveMQQueueConst.QUEUE_NAME_EXTRACT_LOG);  
        }  
        }  
    }  
    return extractLogDestination;  
    }
}
