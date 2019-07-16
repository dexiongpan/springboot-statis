package com.statis.statis.activemq;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ActiveMQQueueConst {
   
    /** 
     * 在Queue模式中，对消息的监听需要对containerFactory进行配置，工厂标识
     */ 
    public static final String BEAN_NAME_JMSLISTENERCONTAINERFACTORY = "queueJmsListenerContainerFactory";    
    
    /**
     * 队列消息标识--请求数据
     */
    public static final String QUEUE_NAME_REQUEST_TEST = "queue.request.test";
    
    /**
     * 之作验证不做数据处理
     * */
    public static final String QUEUE_NAME_VERIFY ="queue.request.verify";
    
    /**
     * 将错误信息存放与数据库 如果之前存在则更新 不存在新建
     * 
     * */
    
    public static final String QUEUE_NAME_DBOPERATE= "queue.db.operate";
    
    public static List<String> brokerURLs(){
    	List<String> urls = new ArrayList<String>();
    	urls.add("tcp://127.0.0.1:9001?tcpNoDelay=true&jms.useAsyncSend=true");
		urls.add("tcp://127.0.0.1:9006?tcpNoDelay=true&jms.useAsyncSend=true");
		return urls;
    } 
    
    
   
    
    
    
}
