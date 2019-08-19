package com.statis.statis.cp.model;

import java.io.Serializable;

public class LauncherBrowsing implements Serializable{

	private static final long serialVersionUID = 1L;
    private String action_type;
    private String pageName;
    private String page_id;
    private String epg_group_id;
    private String log_time;
    private String stb_id;
    private String stb_ip;
    private String stb_mac;
    private String stb_type;
    private String sys_id;
    private String terminal_type;
    private String user_id;
	public String getAction_type() {
		return action_type;
	}
	public void setAction_type(String action_type) {
		this.action_type = action_type;
	}
	public String getPageName() {
		return pageName;
	}
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	public String getPage_id() {
		return page_id;
	}
	public void setPage_id(String page_id) {
		this.page_id = page_id;
	}
	public String getEpg_group_id() {
		return epg_group_id;
	}
	public void setEpg_group_id(String epg_group_id) {
		this.epg_group_id = epg_group_id;
	}
	public String getLog_time() {
		return log_time;
	}
	public void setLog_time(String log_time) {
		this.log_time = log_time;
	}
	public String getStb_id() {
		return stb_id;
	}
	public void setStb_id(String stb_id) {
		this.stb_id = stb_id;
	}
	public String getStb_ip() {
		return stb_ip;
	}
	public void setStb_ip(String stb_ip) {
		this.stb_ip = stb_ip;
	}
	public String getStb_mac() {
		return stb_mac;
	}
	public void setStb_mac(String stb_mac) {
		this.stb_mac = stb_mac;
	}
	public String getStb_type() {
		return stb_type;
	}
	public void setStb_type(String stb_type) {
		this.stb_type = stb_type;
	}
	public String getSys_id() {
		return sys_id;
	}
	public void setSys_id(String sys_id) {
		this.sys_id = sys_id;
	}
	public String getTerminal_type() {
		return terminal_type;
	}
	public void setTerminal_type(String terminal_type) {
		this.terminal_type = terminal_type;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
    
    
}
