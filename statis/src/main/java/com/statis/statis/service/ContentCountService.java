package com.statis.statis.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.statis.statis.mapper.db.ContentCountMapper;
import com.statis.statis.model.ContentCount;
import com.statis.statis.util.Page;
@Service
public class ContentCountService {
    @Autowired
	private ContentCountMapper contentCountMapper;
    
    
    public List<ContentCount> getContentCount(Page page,String name,String contentType,String beginTime,String endTime) {
        return contentCountMapper.selectContentCountPage(page,name,contentType,beginTime,endTime);	
      }
}
