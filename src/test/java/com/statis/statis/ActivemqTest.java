package com.statis.statis;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.statis.statis.activemq.ActiveMQQueueConst;
import com.statis.statis.activemq.producer.ActiveMQQueueProducer;

@Component
@EnableScheduling
public class ActivemqTest {
	@Autowired
    private ActiveMQQueueProducer activeMQQueueProducer;
	
//
//	@Scheduled(fixedRate = 10000, initialDelay = 3000)
//    public void test() {
//        activeMQQueueProducer.sendMsg(ActiveMQQueueConst.QUEUE_NAME_WEBSOCKET_CHATROOM_JAVALSJ,
//                "队列message" + Instant.now().toString());
//    }
	
	
}
