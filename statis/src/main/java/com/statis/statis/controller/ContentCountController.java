package com.statis.statis.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.statis.statis.model.ContentCount;
import com.statis.statis.service.ContentCountService;
import com.statis.statis.util.Page;
import com.statis.statis.vo.PageResult;
@CrossOrigin(origins = "*", maxAge = 3600) 
@RestController
public class ContentCountController {
  @Autowired
 private ContentCountService contentCountService;
   
  @RequestMapping("/content/count/list")
 public PageResult<ContentCount> getContentCount(@RequestParam("page") int currentPage,
		 @RequestParam("pageSize") int pageSize,
		 @RequestParam(value="name",required = false) String name,
		 @RequestParam(value="contentType",required = false) String contentType,
		 @RequestParam(value="beginTime",required = false) String beginTime,
		 @RequestParam(value = "endTime",required = false) String endTime){
	  
	  if(pageSize<0) {
			pageSize = 20;
		}
		if (currentPage <= 0) {
			currentPage = 1;
		}
		System.out.println("name:"+name+"beginTime:"+beginTime+"endTime:"+endTime);
		int currentResult = (currentPage - 1) * pageSize;
		Page page = new Page();
		page.setShowCount(pageSize);
		page.setCurrentResult(currentResult);
		List<ContentCount> resultList = contentCountService.getContentCount(page,name,contentType,beginTime,endTime);
	    int totalCount = page.getTotalResult();
		PageResult<ContentCount> pageResult = new PageResult<ContentCount>();
		pageResult.setTotalCount(totalCount);
		pageResult.setData(resultList);
		return pageResult;
	 
 }
 
  
}
