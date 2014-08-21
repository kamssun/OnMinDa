package com.newthread.android.bean;

import java.io.Serializable;

/**
 * 丢失物品Item
 * @author _foumnder
 *
 */
public class LostListItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;		// 物品名称		
	private String place;		// 拾取地点
	private String time;		// 拾取时间
	private String description;	// 物品描述
	private String type;		// 物品类型
	private String contactOfQQ;		// QQ
	private String contactOfPhone;	
	private String contactOfMail;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getTime() {
		return time;
	}
	public String getType() {
		return type;
	}
	public String getContactOfQQ() {
		return contactOfQQ;
	}
	public void setContactOfQQ(String contactOfQQ) {
		this.contactOfQQ = contactOfQQ;
	}
	public String getContactOfPhone() {
		return contactOfPhone;
	}
	public void setContactOfPhone(String contactOfPhone) {
		this.contactOfPhone = contactOfPhone;
	}
	public String getContactOfMail() {
		return contactOfMail;
	}
	public void setContactOfMail(String contactOfMail) {
		this.contactOfMail = contactOfMail;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
