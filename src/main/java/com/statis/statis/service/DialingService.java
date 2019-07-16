package com.statis.statis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.statis.statis.mapper.db.DialingMapper;
import com.statis.statis.model.DialingTest;
import com.statis.statis.util.Page;

@Service
public class DialingService {
    
	@Autowired
	private DialingMapper dialingMapper;
	
	public List<DialingTest> selectDialingPage(Page page,String title,String isNormal,String beginTime,String endTime){
		return dialingMapper.selectDialingPage(page,title,isNormal,beginTime,endTime);
	}
}
