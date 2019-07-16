package com.statis.statis.activemq.consumer;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.statis.statis.activemq.ActiveMQQueueConst;
import com.statis.statis.mapper.db.DialingMapper;
import com.statis.statis.model.DialingTest;
import com.statis.statis.util.EmptyUtil;
import com.statis.statis.vo.MessageData;

@Component
public class ActiveMQQueueDbConsumer {
	private final static Logger logger = LoggerFactory.getLogger(ActiveMQQueueDbConsumer.class);
    
	@Autowired
	private DialingMapper dialingMapper;

    @JmsListener(destination = ActiveMQQueueConst.QUEUE_NAME_DBOPERATE, containerFactory = ActiveMQQueueConst.BEAN_NAME_JMSLISTENERCONTAINERFACTORY)
    public void receiveQueueDbMsg(MessageData message) {
        ExecutorService service = Executors.newFixedThreadPool(10);
    	service.execute(new Runnable() {
			@Override
			public void run() {
				logger.info("消费了一条队列{}消息{}。", ActiveMQQueueConst.QUEUE_NAME_DBOPERATE, message);
		        String itemCode=message.getItemCode();
		        String dataLink=message.getDataLink();
		        String itemTitle=message.getItemTitle();
		        String itemType=message.getItemType();
		        String status=message.getStatus();
		        if(EmptyUtil.isNotEmpty(itemCode)) {
		        	DialingTest dialingTest = new DialingTest();
		        	dialingTest.setItemCode(itemCode);
		        	if(EmptyUtil.isNotEmpty(itemTitle)) {dialingTest.setItemTitle(itemTitle);}
		        	String poscode=message.getPoscode();
		        	if(EmptyUtil.isNotEmpty(poscode)) {
		        		dialingTest.setPoscode(poscode);
		        	}
		        	if(EmptyUtil.isNotEmpty(itemTitle)) {dialingTest.setItemTitle(itemTitle);}
		        	if(EmptyUtil.isNotEmpty(itemType)) {dialingTest.setItemType(itemType);}
		        	if(EmptyUtil.isNotEmpty(dataLink)) {dialingTest.setPath(dataLink);}
		        	if(EmptyUtil.isNotEmpty(status)) {
		        		dialingTest.setStatusCode(status);
		        		if(status.equals("200")) {dialingTest.setIsNormal(0);}
		        		else {dialingTest.setIsNormal(1);}
		        	}
		        	
//		        	long count=dialingMapper.isExists(dialingTest);
//		        	if(count>0&&!dialingTest.getStatusCode().equals("200")){//更新
//		        		dialingTest.setUpdateTime(new Date());
//		        		dialingMapper.update(dialingTest);
//		        	}else if(count<=0&&!dialingTest.getStatusCode().equals("200")){
//		        		dialingTest.setCreateTime(new Date());
//		        		dialingMapper.insert(dialingTest);	
//		        	}
		        	//dialingTest.setCreateTime(getNowDate());
	        		dialingMapper.insert(dialingTest);	
		        }
				
			}});
    }
    
    private Date getNowDate() {
    	 Date currentTime = new Date();
    	   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	   String dateString = formatter.format(currentTime);
    	   ParsePosition pos = new ParsePosition(8);
    	   Date currentTime_2 = formatter.parse(dateString, pos);
    	   return currentTime_2;
    	}
}
