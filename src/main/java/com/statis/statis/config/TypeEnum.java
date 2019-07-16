package com.statis.statis.config;

public enum TypeEnum {
	BIZ("biz"),SERIES("series"),VOD("vod"),EPISODE("episode");
	private String value;
    
	TypeEnum(String value) {
        this.value = value;
    }
	
	 public String getValue() {
	        return value;
	    }
}
