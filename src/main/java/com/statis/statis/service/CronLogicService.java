package com.statis.statis.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.statis.statis.activemq.ActiveMQQueueConst;
import com.statis.statis.activemq.producer.ActiveMQQueueProducer;
import com.statis.statis.config.TypeEnum;
import com.statis.statis.mapper.db.DialingMapper;
import com.statis.statis.vo.MessageData;

@Component
public class CronLogicService {
	private static final String iptv= "http://183.224.6.2:7095/epg/api/page/biz_90057550.json";
	private static final String srlb= "http://183.224.6.2:7095/epg/api/page/biz_18127105.json";
	private static final String grzx= "http://183.224.6.2:7095/epg/api/page/biz_90057550.json";
	private final static Logger logger = LoggerFactory.getLogger(CronLogicService.class);
	@Autowired
    private ActiveMQQueueProducer activeMQQueueProducer;
	
	@Autowired
	private DialingMapper dialingMapper;
	
	 
	@Scheduled(cron = "0 */50 * * * ?")
	public void  cronLogic() {
		//清空链接缓存和数据库表
		System.out.println(activeMQQueueProducer.getPathMap().size());
		activeMQQueueProducer.getPathMap().clear();
		dialingMapper.clear();
		//云南移动IPTV拨测
		MessageData message = new MessageData();
		message.setDataLink(iptv);
		message.setItemType(TypeEnum.BIZ.getValue());
		message.setItemTitle("第二代云南移动IPTV");
		message.setItemCode("biz_90057550");
		activeMQQueueProducer.sendMsgData(ActiveMQQueueConst.QUEUE_NAME_REQUEST_TEST, message);
		
		//少儿频道
		message.setDataLink(srlb);
		message.setItemType(TypeEnum.BIZ.getValue());
		message.setItemTitle("新版少儿列表");
		message.setItemCode("biz_18127105");
		activeMQQueueProducer.sendMsgData(ActiveMQQueueConst.QUEUE_NAME_REQUEST_TEST, message);
		//个人中心
		message.setDataLink(grzx);
		message.setItemType(TypeEnum.BIZ.getValue());
		message.setItemTitle("新版个人中心");
		message.setItemCode("biz_56393102");
		activeMQQueueProducer.sendMsgData(ActiveMQQueueConst.QUEUE_NAME_REQUEST_TEST, message);
	}
}
