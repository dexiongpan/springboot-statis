package com.statis.statis.mapper.db;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.statis.statis.model.User;

@Mapper
public interface UserMapper {
  public List<User> selectUsers();
}
