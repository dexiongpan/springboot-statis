package com.statis.statis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.statis.statis.config.TargetDataSource;
import com.statis.statis.mapper.db.UserMapper;
import com.statis.statis.model.User;
import com.statis.statis.util.EmptyUtil;
import com.statis.statis.vo.ResponseResult;

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
    
    public Object getUser(User user) {
    	User user2 = userMapper.selectUser(user);
    	ResponseResult<User>responseResult = new ResponseResult<User>();
    	 if (EmptyUtil.isEmpty(user2)) {
    		 responseResult.setCode("250");
    		 responseResult.setMsg("用户信息不存在");
    		 return responseResult;
		}
    	 if(!user.getPassword().equals(user2.getPassword())) {
    		 responseResult.setCode("500");
    		 responseResult.setMsg("账号或密码错误！");
    		 responseResult.setData(user);
    		 return responseResult;
    	 }
    	 responseResult.setCode("200");
    	 responseResult.setData(user2);
		 responseResult.setMsg("请求成功"); 
        return responseResult;	
      }
}
