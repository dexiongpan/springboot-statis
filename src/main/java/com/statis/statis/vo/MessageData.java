package com.statis.statis.vo;

import java.io.Serializable;



public class MessageData  implements Serializable{
 static final long serialVersionUID = 1L;
	private String itemType;
	private String dataLink;
	private String itemCode;
	private String itemTitle;
	private String status;
	private String poscode;
	
	public String getPoscode() {
		return poscode;
	}
	public void setPoscode(String poscode) {
		this.poscode = poscode;
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public String getDataLink() {
		return dataLink;
	}
	public void setDataLink(String dataLink) {
		this.dataLink = dataLink;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemTitle() {
		return itemTitle;
	}
	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
