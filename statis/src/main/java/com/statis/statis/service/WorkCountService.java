package com.statis.statis.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.statis.statis.mapper.db.WorkCountMapper;
import com.statis.statis.model.WorkCount;
import com.statis.statis.util.Page;

@Service
public class WorkCountService {
    @Autowired
	private WorkCountMapper workCountMapper;
    
    
    public List<WorkCount> getWorkCountListPage(Page page,String name,String beginTime,String endTime) {
        return workCountMapper.getWorkCountListPage(page,name,beginTime,endTime);	
      }
}
