package com.statis.statis.mapper.db;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.statis.statis.model.ContentCount;
import com.statis.statis.util.Page;

@Mapper
public interface ContentCountMapper {
	
  public List<ContentCount> selectContentCountPage(@Param("page") Page page,String name,String contentType,String beginTime,String endTime);
  
}
