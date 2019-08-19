package com.statis.statis.config;

public enum  DBTypeEnum {
   
	epg("epg"), mobile("mobile"),statis("statis");
    private String value;

    DBTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
