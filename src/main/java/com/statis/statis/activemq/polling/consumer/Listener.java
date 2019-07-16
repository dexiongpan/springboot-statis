package com.statis.statis.activemq.polling.consumer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.statis.statis.activemq.ActiveMQQueueConst;
import com.statis.statis.activemq.producer.ActiveMQQueueProducer;
import com.statis.statis.util.CheckUrl;
import com.statis.statis.util.EmptyUtil;
import com.statis.statis.vo.MessageData;

@Component
class Listener implements MessageListener {
	private final static Logger logger = LoggerFactory.getLogger(Listener.class);
	
	@Autowired
    private ActiveMQQueueProducer activeMQQueueProducer;
	
	@Override
	public void onMessage(Message message) {
		        //使用线程池处理
        ExecutorService service = Executors.newFixedThreadPool(10);
		        	service.execute(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								ActiveMQObjectMessage objectMessage  = (ActiveMQObjectMessage)message;
								MessageData messageData = (MessageData) objectMessage.getObject();
								 logger.info("消费了一条队列{}消息{}。", ActiveMQQueueConst.QUEUE_NAME_VERIFY, messageData);
						        String dataLink = messageData.getDataLink();
//						        dataLink+="111";
//						        message.setDataLink(dataLink);
						        if(EmptyUtil.isNotEmpty(dataLink)) {
						        String status=CheckUrl.isConnect(dataLink);
						        if(EmptyUtil.isEmpty(status)&&!status.equals("200")) {
						        	messageData.setStatus(status);
						         activeMQQueueProducer.sendMsgData(ActiveMQQueueConst.QUEUE_NAME_DBOPERATE, messageData);
						        }}
						} catch (JMSException e) {
							// TODO Auto-generated catch block
							logger.error(e.toString());
						}
						}});
    }
}

