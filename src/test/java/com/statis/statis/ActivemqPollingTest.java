//package com.statis.statis;
//
//import java.time.Instant;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import com.statis.statis.activemq.ActiveMQQueueConst;
//import com.statis.statis.activemq.polling.producer.GroupQueueSender;
//import com.statis.statis.activemq.producer.ActiveMQQueueProducer;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class ActivemqPollingTest {
//	@Autowired
//    private GroupQueueSender activeMQQueueProducer;
//	
//
//	
//	@Test
//    public void test() throws Exception {
//		activeMQQueueProducer.setBrokerURLs(ActiveMQQueueConst.brokerURLs());
//		activeMQQueueProducer.setGroupQueueName(ActiveMQQueueConst.QUEUE_NAME_VERIFY);
//		activeMQQueueProducer.setMaxConnections(1);
//		activeMQQueueProducer.start();
//        activeMQQueueProducer.sendMsg("111111");
//    }
//	
//	
//}
