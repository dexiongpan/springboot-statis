package com.statis.statis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.statis.statis.config.TargetDataSource;
import com.statis.statis.mapper.db.UserMapper;
import com.statis.statis.model.User;

@Service
public class UserService {
    @Autowired
	private UserMapper userMapper;
    
    @TargetDataSource("db1")
    public List<User> getUsers() {
      return userMapper.selectUsers();	
    }
    
    @TargetDataSource("db2")
    public List<User> getD2Users() {
        return userMapper.selectUsers();	
      }
}
