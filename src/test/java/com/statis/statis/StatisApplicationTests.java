package com.statis.statis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.statis.statis.activemq.ActiveMQQueueConst;
import com.statis.statis.activemq.producer.ActiveMQQueueProducer;
import com.statis.statis.config.TypeEnum;
import com.statis.statis.vo.MessageData;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StatisApplicationTests {
	private static final String iptv= "http://183.224.6.2:7095/epg/api/page/biz_90057550.json";
	@Autowired
    private ActiveMQQueueProducer activeMQQueueProducer;
	@Test
	public void contextLoads() {
		//云南移动IPTV拨测
		MessageData message = new MessageData();
		message.setDataLink(iptv);
		message.setItemType(TypeEnum.BIZ.getValue());
		message.setItemTitle("第二代云南移动IPTV");
		message.setItemCode("biz_90057550");
		activeMQQueueProducer.sendMsgData(ActiveMQQueueConst.QUEUE_NAME_REQUEST_TEST, message);
	}

}
