package com.statis.statis.activemq.producer;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.jms.Destination;

import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.statis.statis.activemq.ActiveMQQueueConst;
import com.statis.statis.activemq.DestinationFactory;
import com.statis.statis.activemq.polling.producer.GroupQueueSender;
import com.statis.statis.util.EmptyUtil;
import com.statis.statis.vo.MessageData;
import com.statis.statis.vo.ResultData;

@Component
public class ActiveMQQueueProducer {
	private final static Logger logger = LoggerFactory.getLogger(ActiveMQQueueProducer.class);

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;
    private  ConcurrentMap<String, String> pathMap = new ConcurrentHashMap<>();
    @Autowired
    private GroupQueueSender groupQueueSender;
    /** 
     * 发送队列消息
     * @param destinationName 消息目的地标识
     * @param messageData 数组对象
     * @param message 对象消息
     */ 
    public void sendMsg(String destinationName, ResultData<JSONArray> messageData) {
        logger.info(destinationName);
        JSONArray jsonArray = messageData.getData();
        String type = messageData.getType();
//        Destination destination = new ActiveMQQueue(destinationName);
        Destination destination = DestinationFactory.getDestination(destinationName);
        if(jsonArray.size()>0) {
        	switch (type) {
			case "biz":
				handleBizData(destination,jsonArray);
				
				break;
			case "series":
				handleSerIesData(destination,jsonArray);
				break;
			default:
				break;
			}
         
        
         }
    }
    
    public void sendMsgData(String destinationName,MessageData message)  {
//    	Destination destination = new ActiveMQQueue(destinationName);
//    	if(destinationName.equals(ActiveMQQueueConst.QUEUE_NAME_VERIFY)) {
//    		try {
//				groupQueueSender.sendMsg(message);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//    	}else {
    	Destination destination = DestinationFactory.getDestination(destinationName);
    	jmsMessagingTemplate.convertAndSend(destination, message);
//    	}
    }
    
    
    
    
    //处理biz类型data
    private void handleBizData(Destination destination,JSONArray jsonArray) {
    	for(int i =0 ;i<jsonArray.size();i++) {
        	JSONObject job = jsonArray.getJSONObject(i);
        	String itemType=(String) job.get("itemType");
        	if(EmptyUtil.isNotEmpty(itemType)) {//itemtype为空不处理
        	String dataLink = (String)job.get("dataLink");
        	String vImg = job.getString("vImg");
           	MessageData message= new MessageData();
        	message.setItemType(itemType);
        	message.setItemCode((String) job.get("itemCode"));
        	String poscode=(String) job.get("poscode");
        	if(EmptyUtil.isNotEmpty(poscode)) {
        		message.setPoscode(poscode);
        	}
        	message.setItemTitle((String) job.get("itemTitle"));
        	//该vImg放到验证队列处理
        	if(EmptyUtil.isNotEmpty(vImg)) {
        		message.setDataLink(vImg);
        		sendMsgData(ActiveMQQueueConst.QUEUE_NAME_VERIFY,message);
        	}
        	String itemIcon = job.getString("itemIcon");//放到验证队列处理
        	if(EmptyUtil.isNotEmpty(itemIcon)) {
        		message.setDataLink(itemIcon);
        		sendMsgData(ActiveMQQueueConst.QUEUE_NAME_VERIFY,message);
        	}
        	if(EmptyUtil.isNotEmpty(dataLink)){
        		message.setDataLink(dataLink);
            	jmsMessagingTemplate.convertAndSend(destination, message);
        	}
        	}
        }  
    }
    
    //处理series类型数据
    private void handleSerIesData(Destination destination,JSONArray jsonArray) {
    	for(int i =0 ;i<jsonArray.size();i++) {
        	JSONObject job = jsonArray.getJSONObject(i);
        	String dataLink = (String)job.get("dataLink");
        	MessageData message= new MessageData();
        	message.setItemCode((String) job.get("code"));
        	message.setItemTitle((String) job.get("title"));
        	//该dataLink放到验证队列验证
        	if(EmptyUtil.isNotEmpty(dataLink)) {
        	message.setDataLink(dataLink);
        	sendMsgData(ActiveMQQueueConst.QUEUE_NAME_VERIFY,message);
        	}
        	String vImg = job.getString("vImg");
        	//改 vImg放到验证队列处理
        	if(EmptyUtil.isNotEmpty(vImg)) {
        	message.setDataLink(vImg);
        	sendMsgData(ActiveMQQueueConst.QUEUE_NAME_VERIFY,message);
        	}
//        	jmsMessagingTemplate.convertAndSend(destination, message);迭代到子集详情
        } 
    }

	public ConcurrentMap<String, String> getPathMap() {
		return pathMap;
	}

    
//    private GroupQueueSender getGroupQueueSender() throws Exception{
//    	groupQueueSender.start();
//    	return groupQueueSender;
//    }
    
    
    
}
