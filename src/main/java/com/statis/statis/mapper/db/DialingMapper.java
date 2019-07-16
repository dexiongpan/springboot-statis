package com.statis.statis.mapper.db;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.statis.statis.model.DialingTest;
import com.statis.statis.util.Page;

@Mapper
public interface DialingMapper {
  
	public List<DialingTest> selectDialingPage(@Param("page")Page page,@Param("title") String title,@Param("isNormal") String  isNormal,@Param("beginTime") String beginTime,@Param("endTime") String endTime);
	
	public Integer insert(DialingTest dialingTest);
	
	public void update(DialingTest dialingTest);
	
	public long isExists(DialingTest dialingTest);
	
	@Update("delete from dialingtest")
	public void clear();
}
