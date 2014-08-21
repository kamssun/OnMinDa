package com.newthread.android.bean;

import android.graphics.Bitmap;

public class NewsContentVo {
	private String imageUrl;
	private Bitmap bitmap;	// 新闻图片
	private String title;	// 标题
	private String time;	// 时间
	private String content;	// 内容
	private String source; 	// 来源及编辑
//	private String auther;	// 作者
	
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
