package com.statis.statis.mapper.db;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.statis.statis.model.User;

@Mapper
public interface UserMapper {
  public List<User> selectUsers();
  
  @Select("<script>"
		  +" select id,name,username,password,avater"
		  +" from user u"
		  +" where u.username=#{user.username}"
		 +"</script>")
  public User selectUser(@Param("user") User user);
}
