package com.statis.statis.mapper.db;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.statis.statis.model.WorkCount;
import com.statis.statis.util.Page;

@Mapper
public interface WorkCountMapper {
	
  public List<WorkCount> getWorkCountListPage(@Param("page") Page page,String name,String beginTime,String endTime);
  
}
