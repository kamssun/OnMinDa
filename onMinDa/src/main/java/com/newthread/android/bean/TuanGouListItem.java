package com.newthread.android.bean;

public class TuanGouListItem {
	// //////////////////
	// 团购
	private String title;			// 标题
	
	private String dealURL;			// web链接
	private String description;		// 团购描述
	private String originalPrice;	// 团购包含商品原价值
	private String currentPrice;  	// 团购价格
	private String s_image_url;		// 图片
	private String purchase_count;	// 购买人数
	
	public String getPurchase_count() {
		return purchase_count;
	}
	public void setPurchase_count(String purchase_count) {
		this.purchase_count = purchase_count;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDealURL() {
		return dealURL;
	}
	public void setDealURL(String dealURL) {
		this.dealURL = dealURL;
	}
	public String getS_image_url() {
		return s_image_url;
	}
	public void setS_image_url(String s_image_url) {
		this.s_image_url = s_image_url;
	}
	public String getOriginalPrice() {
		return originalPrice;
	}
	public void setOriginalPrice(String originalPrice) {
		this.originalPrice = originalPrice;
	}
	public String getCurrentPrice() {
		return currentPrice;
	}
	public void setCurrentPrice(String currentPrice) {
		this.currentPrice = currentPrice;
	}
}
