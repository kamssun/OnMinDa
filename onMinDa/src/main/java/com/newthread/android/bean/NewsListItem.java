package com.newthread.android.bean;

/*
 * 新闻列表Item
 */
public class NewsListItem {
	private String id;		// 数据id
	private String type;	// 该条新闻的类型
	
	private String title;	// 标题
	private String digest;	// 概要
	private String time;	// 时间
	private String href;	// 该item链接
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDigest() {
		return digest;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public void setDigest(String digest) {
		this.digest = digest;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	
}
