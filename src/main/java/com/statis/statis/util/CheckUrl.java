package com.statis.statis.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.UrlResource;

import com.statis.statis.activemq.consumer.ActiveMQQueueVerifyConsumer;



public class CheckUrl {
	private final static Logger logger = LoggerFactory.getLogger(CheckUrl.class);
		  private static URL urlStr;
		  private static HttpURLConnection connection;

		  private static String state;
		  public static  String isConnect(String url) {
				try {
					urlStr = new URL(url);
					connection = (HttpURLConnection) urlStr.openConnection();
					
					connection.setConnectTimeout(20000);
					state = String.valueOf(connection.getResponseCode());
					return state;
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					logger.error("{}:格式不正确",url);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error("{}:IOException异常",url);
				}
			return state;

		  }
		  
		  public static String getStatusCode(String url) {
			  try {  
				   URL u = new URL(url);  
				   try {  
				       HttpURLConnection uConnection = (HttpURLConnection) u.openConnection();  
				       try {  
				           uConnection.connect();  
				           state = String.valueOf(uConnection.getResponseCode());  
                           return state;
				       } catch (Exception e) {  
				        
				           e.printStackTrace();  
				           System.out.println("connect failed");  
				       }                    
				   } catch (IOException e) {  
				       System.out.println("build failed");  
				       e.printStackTrace();  
				   }  
				              
				   } catch (MalformedURLException e) {  
				       System.out.println("build url failed");  
				       e.printStackTrace();  
				} 
			 return state;
		  }
		  
			public static String checkValidIP(String address){
				URL url;
				try {
					url = new URL(address);
					HttpURLConnection connection = null;
					try {
						connection = (HttpURLConnection) url.openConnection();
//						connection.setReadTimeout(20000);
						connection.setConnectTimeout(20000);
						state = String.valueOf(connection.getResponseCode());
						return state;
					} catch (Exception e) {
						connection.disconnect();
						logger.error("{}:IOException异常{}",url,e.toString());
						return state;
					}
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					connection.disconnect();
					System.out.println("build url failed");  
				}finally{
		            if(connection!=null)
		            	connection.disconnect();
				}
              return state;
			}
			
			public static String isExists(String address)  {
				try {
					return (new UrlResource(address).exists()==true)?"200":"404";
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					System.out.println("build url failed"); 
					e.printStackTrace();
				}
				return "404";
			}
			
		}
