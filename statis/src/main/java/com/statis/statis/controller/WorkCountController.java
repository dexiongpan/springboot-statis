package com.statis.statis.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.statis.statis.model.WorkCount;
import com.statis.statis.service.WorkCountService;
import com.statis.statis.util.Page;
import com.statis.statis.vo.PageResult;

@CrossOrigin(origins = "*", maxAge = 3600) 
@RestController
public class WorkCountController {
  @Autowired
 private WorkCountService workCountService;
  
  @RequestMapping("/work/count/list")
 public PageResult<WorkCount> getWorkCountListPage(@RequestParam("page") int currentPage,@RequestParam("pageSize") int pageSize,
		 @RequestParam(value="name",required = false) String name,
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
		List<WorkCount> resultList = workCountService.getWorkCountListPage(page,name,beginTime,endTime);
		int totalCount = page.getTotalResult();
		PageResult<WorkCount> pageResult = new PageResult<WorkCount>();
		pageResult.setTotalCount(totalCount);
		pageResult.setData(resultList);
	 return pageResult;
	 
	 
	 
	 
 }
 
  
}
