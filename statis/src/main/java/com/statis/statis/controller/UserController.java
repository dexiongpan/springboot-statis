package com.statis.statis.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.statis.statis.model.User;
import com.statis.statis.service.UserService;
@CrossOrigin(origins = "*", maxAge = 3600) 
@RestController
public class UserController {
  @Autowired
 private UserService userService;
  
  @RequestMapping("db1/user")
 public List<User> geDb1tUsers(){
	 return userService.getUsers();
 }
  
  @RequestMapping("db2/user")
 public List<User> geDb2tUsers(){
	 return userService.getD2Users();
 }
  
  @RequestMapping(value="/login",method = RequestMethod.POST)
  public Object geUser(@RequestBody User user){
 	 return  userService.getUser(user);
  }
}
