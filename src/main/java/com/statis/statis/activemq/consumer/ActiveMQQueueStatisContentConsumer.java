package com.statis.statis.activemq.consumer;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.statis.statis.activemq.ActiveMQQueueConst;
import com.statis.statis.model.ContentCount;
import com.statis.statis.service.ContentCountService;
import com.statis.statis.util.EmptyUtil;

@Component
public class ActiveMQQueueStatisContentConsumer {
  
	private final static Logger logger = LoggerFactory.getLogger(ActiveMQQueueStatisContentConsumer.class);
	
	@Autowired
	private ContentCountService contentService;
	
	 @JmsListener(destination = ActiveMQQueueConst.QUEUE_NAME_STATIS_COTENT, containerFactory = ActiveMQQueueConst.BEAN_NAME_JMSLISTENERCONTAINERFACTORY)
	 public void receiveQueueRequestMsg(ContentCount message)  {
		 ContentCount contentCount = message;
		 String code = message.getContentCode();
		 if (EmptyUtil.isNotEmpty(code)) {
			List<ContentCount>create_online =contentService.getEpgName(code);
			if(create_online.size()==1) {
				contentCount.setCreater(create_online.get(0).getCreater());
				contentCount.setEdit_mode(create_online.get(0).getEdit_mode());
				contentCount.setAudit_status(create_online.get(0).getAudit_status());
				contentCount.setStatus(create_online.get(0).getStatus());
				contentCount.setEnable_status(create_online.get(0).getEnable_status());
			}
			if(create_online.size()==2) {
				contentCount.setCreater(create_online.get(0).getCreater());
				contentCount.setOnline(create_online.get(1).getCreater());
				contentCount.setEdit_mode(create_online.get(0).getEdit_mode());
				contentCount.setAudit_status(create_online.get(0).getAudit_status());
				contentCount.setStatus(create_online.get(0).getStatus());
				contentCount.setEnable_status(create_online.get(0).getEnable_status());
			}
			dbInsert(contentCount);
		}else {
			logger.info("content code is null,contentCode:"+code);
		} 
	 }
	 
	 private void dbInsert(ContentCount content) {
	        ExecutorService service = Executors.newFixedThreadPool(10);
	    	service.execute(new Runnable() {
				@Override
				public void run() {
					 try {
						 contentService.insert(content);
						 Thread.sleep(2000);
					} catch (ParseException  e) {
						// TODO Auto-generated catch block
						logger.debug(e.toString());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						logger.debug(e.toString());
					}
					}
				});
	 }
	
}
