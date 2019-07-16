package com.statis.statis.activemq.consumer;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.statis.statis.activemq.ActiveMQQueueConst;
import com.statis.statis.activemq.producer.ActiveMQQueueProducer;
import com.statis.statis.util.CheckUrl;
import com.statis.statis.util.EmptyUtil;
import com.statis.statis.vo.MessageData;

@Component
public class ActiveMQQueueVerifyConsumer {
	private final static Logger logger = LoggerFactory.getLogger(ActiveMQQueueVerifyConsumer.class);
     
//	@Autowired
//     private HttpClientUtil HttpClientUtil;
     
	@Autowired
    private ActiveMQQueueProducer activeMQQueueProducer;
	
    @JmsListener(destination = ActiveMQQueueConst.QUEUE_NAME_VERIFY, containerFactory = ActiveMQQueueConst.BEAN_NAME_JMSLISTENERCONTAINERFACTORY)
    public void receiveQueueVerifyMsg(MessageData message) throws ParseException, ClientProtocolException, IOException {
//        logger.info("消费了一条队列{}消息{}。", ActiveMQQueueConst.QUEUE_NAME_VERIFY, message);
//        String dataLink = message.getDataLink();
////        dataLink+="111";
////        message.setDataLink(dataLink);
//        if(EmptyUtil.isNotEmpty(dataLink)) {
//         //String status=String.valueOf(HttpClientUtil.getObject(dataLink).get("status"));
//        String status=CheckUrl.isConnect(dataLink);
//        if(EmptyUtil.isEmpty(status)&&!status.equals("200")) {
//         message.setStatus(status);
//         activeMQQueueProducer.sendMsgData(ActiveMQQueueConst.QUEUE_NAME_DBOPERATE, message);
//        }
//        }
        ExecutorService service = Executors.newFixedThreadPool(10);
    	service.execute(new Runnable() {
			@Override
			public void run() {
				logger.info("消费了一条队列{}消息{}。", ActiveMQQueueConst.QUEUE_NAME_VERIFY, message);
				String dataLink = message.getDataLink();
//			        dataLink+="111";
//			        message.setDataLink(dataLink);
				if(EmptyUtil.isNotEmpty(dataLink)) {
				String status=CheckUrl.isExists(dataLink);
				//System.out.print("错误url:"+dataLink+status);
				if(EmptyUtil.isNotEmpty(status)&&!status.equals("200")) {
					message.setStatus(status);
				 activeMQQueueProducer.sendMsgData(ActiveMQQueueConst.QUEUE_NAME_DBOPERATE, message);
				}}
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					logger.debug(e.toString());
				}
			}});
    }
}
