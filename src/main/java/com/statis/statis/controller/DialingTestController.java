package com.statis.statis.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.statis.statis.model.DialingTest;
import com.statis.statis.service.DialingService;
import com.statis.statis.util.Page;
import com.statis.statis.vo.PageResult;

@CrossOrigin(allowCredentials = "true", allowedHeaders = "*") 
@RestController
public class DialingTestController {
   
	@Autowired
	private DialingService dialingService;
	
	@RequestMapping(value = "/dialing/count/list",method = RequestMethod.GET)
	 public PageResult<DialingTest> getContentCount(@RequestParam("page") int currentPage,
			 @RequestParam("pageSize") int pageSize,
			 @RequestParam(value="title",required = false) String title,
			 @RequestParam(value="isNormal",required = false) String isNormal,
			 @RequestParam(value="beginTime",required = false) String beginTime,
			 @RequestParam(value = "endTime",required = false) String endTime){
		  
		  if(pageSize<0) {
				pageSize = 20;
			}
			if (currentPage <= 0) {
				currentPage = 1;
			}
			System.out.println("name:"+title+"isNormal:"+isNormal+"beginTime:"+beginTime+"endTime:"+endTime);
			int currentResult = (currentPage - 1) * pageSize;
			Page page = new Page();
			page.setShowCount(pageSize);
			page.setCurrentResult(currentResult);
			List<DialingTest> resultList = dialingService.selectDialingPage(page,title,isNormal,beginTime,endTime);
		    int totalCount = page.getTotalResult();
			PageResult<DialingTest> pageResult = new PageResult<DialingTest>();
			pageResult.setTotalCount(totalCount);
			pageResult.setData(resultList);
			return pageResult;
		 
	 }
}
