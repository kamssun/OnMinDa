package com.newthread.android.bean;

/**
 * 图书收藏
 * 
 * @author _foumnder
 * 
 */
public class BookCollectItem {
	private String ID;	// 数据库ID
	private String addTime;	// 存入数据库时间
	private String type;	// 存入类型
	private String url;		// 链接
	
	private String title;
	private String auther;
	private String id;
	private String publisher;
	private String place;
	
	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public BookCollectItem() {
		this.title = "";
		this.auther = "";
		this.id = "";
		this.publisher = "";
	}

	public BookCollectItem(String title, String auther, String id,
			String publisher, String place) {
		this.title = title;
		this.auther = auther;
		this.id = id;
		this.publisher = publisher;
		this.place = place;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuther() {
		return auther;
	}

	public void setAuther(String auther) {
		this.auther = auther;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

}
