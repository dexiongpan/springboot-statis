package com.statis.statis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.statis.statis.activemq.ActiveMQQueueConst;
import com.statis.statis.activemq.producer.ActiveMQQueueStatisProducer;
import com.statis.statis.mapper.db.ContentCountMapper;
import com.statis.statis.model.ContentCount;
import com.statis.statis.util.EmptyUtil;
import com.statis.statis.util.Page;

@Component
public class CronStatisContentService {

	@Autowired
	private ContentCountService contentCountService;
	
	@Autowired
	private ActiveMQQueueStatisProducer statisProducer;
	
	@Autowired
	private ContentCountMapper contentMapper;
	
	private static final int PAGE_SIZE= 10000;
	private static final int CURRENT_PAGE =0;
	@Scheduled(cron = "0 0 * * * ?")
	public void cronStatisContent() {
		//清空表
	    System.out.println("statis 启动");
		contentMapper.clear();
		int pageSize = PAGE_SIZE;
		int currentPage = CURRENT_PAGE;
		
		if(pageSize<0) {
			pageSize = 10000;
		}
		if (currentPage <= 0) {
			currentPage = 1;
		}
		
		int currentResult = (currentPage - 1) * pageSize;
		Page page = new Page();
		page.setShowCount(pageSize);
		page.setCurrentResult(currentResult);
		List<ContentCount> contentCounts=contentCountService.getEpgContent(page);
		handData(contentCounts);
		int totalCount = page.getTotalResult();
		int totalPage = (int) Math.ceil(Double.valueOf(totalCount)/pageSize);
		System.out.println("totalCount:"+totalCount+"page:"+totalPage);
		if(totalPage>1) {
			for(int i =2;i<=totalPage;i++) {
				currentPage = i;
				currentResult = (currentPage - 1) * pageSize;
				page.setCurrentResult(currentResult);
				contentCounts=contentCountService.getEpgContent(page);
				handData(contentCounts);
			}
		}
		
	}
	
	private void handData(List<ContentCount> results) {
		if(EmptyUtil.isNotEmpty(results)) {
			for(ContentCount content:results) {
				//放到队列处理
				statisProducer.sendMsgData(ActiveMQQueueConst.QUEUE_NAME_STATIS_COTENT, content);
			}
			
		}
	}
}
