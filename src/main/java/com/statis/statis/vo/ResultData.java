package com.statis.statis.vo;

import java.io.Serializable;

public class ResultData<T> implements Serializable{

	private static final long serialVersionUID = 1L;
	private String type;
	private T data;
	private boolean isErgodic;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public boolean isErgodic() {
		return isErgodic;
	}
	public void setErgodic(boolean isErgodic) {
		this.isErgodic = isErgodic;
	}
	

}
