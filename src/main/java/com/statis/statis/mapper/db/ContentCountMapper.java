package com.statis.statis.mapper.db;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.statis.statis.model.ContentCount;
import com.statis.statis.util.Page;

@Mapper
public interface ContentCountMapper {
	
  public List<ContentCount> selectContentCountPage(@Param("page") Page page,String name,String contentType,String beginTime,String endTime);
  
  public List<ContentCount> selectepgContentPage(@Param("page") Page page);
  
  public List<ContentCount> getNameByCode(String code);
  
  
  int insert(ContentCount content);

	@Update("delete from content_account")
	public void clear();

}
