package com.statis.statis.model;

import java.util.Date;

import org.apache.ibatis.type.Alias;

@Alias("workCount")
public class WorkCount {
  
	private String  id;
	private String publishedName;
	//浏览量
	private int pageViewCount;
	//发稿量
	private int publishedCount;
	private Date onlineTime;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPublishedName() {
		return publishedName;
	}
	public void setPublishedName(String publishedName) {
		this.publishedName = publishedName;
	}
	public int getPageViewCount() {
		return pageViewCount;
	}
	public void setPageViewCount(int pageViewCount) {
		this.pageViewCount = pageViewCount;
	}
	public int getPublishedCount() {
		return publishedCount;
	}
	public void setPublishedCount(int publishedCount) {
		this.publishedCount = publishedCount;
	}
	public Date getOnlineTime() {
		return onlineTime;
	}
	public void setOnlineTime(Date onlineTime) {
		this.onlineTime = onlineTime;
	}
	
	
}
