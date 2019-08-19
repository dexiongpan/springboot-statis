package com.statis.statis.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonFormat;

@Alias("contentCount")
public class ContentCount  implements Serializable{
  
	private static final long serialVersionUID = 1L;

	private String id;

	private String contentCode;
	private String contentName;
	private  String contentType;
	private int truePageViewCount;
	private int fakePageViewCount;
	private String creater;
	private String online;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	private Date onlineTime;
	
	//审核状态、编辑类型
	private String edit_mode;
	private int status;
	private int enable_status;
	private int audit_status;
	
	
	public String getEdit_mode() {
		return edit_mode;
	}
	public void setEdit_mode(String edit_mode) {
		this.edit_mode = edit_mode;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getEnable_status() {
		return enable_status;
	}
	public void setEnable_status(int enable_status) {
		this.enable_status = enable_status;
	}
	public int getAudit_status() {
		return audit_status;
	}
	public void setAudit_status(int audit_status) {
		this.audit_status = audit_status;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContentCode() {
		return contentCode;
	}
	public void setContentCode(String contentCode) {
		this.contentCode = contentCode;
	}
	public String getContentName() {
		return contentName;
	}
	public void setContentName(String contentName) {
		this.contentName = contentName;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public int getTruePageViewCount() {
		return truePageViewCount;
	}
	public void setTruePageViewCount(int truePageViewCount) {
		this.truePageViewCount = truePageViewCount;
	}
	public int getFakePageViewCount() {
		return fakePageViewCount;
	}
	public void setFakePageViewCount(int fakePageViewCount) {
		this.fakePageViewCount = fakePageViewCount;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public String getOnline() {
		return online;
	}
	public void setOnline(String online) {
		this.online = online;
	}
	public Date getOnlineTime() {
		return onlineTime;
	}
	public void setOnlineTime(Date onlineTime) {
		this.onlineTime = onlineTime;
	}
	
	
	
}
