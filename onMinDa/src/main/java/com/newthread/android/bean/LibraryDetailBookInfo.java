package com.newthread.android.bean;

import java.util.ArrayList;
import java.util.List;

public class LibraryDetailBookInfo {
	private String title; 		// 标题
	private String auther; 		// 作者
	private String publisher; 	// 出版社

	private String douBanHref; 	// 豆瓣链接
	private String digest; 		// 文摘附注
	private String intro;		// 简介
	private List<LibraryDetailListItemInfo> list; // �ݲ���Ϣ�б�

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public LibraryDetailBookInfo() {
		this.list = new ArrayList<LibraryDetailListItemInfo>();
	}
	
	public String getDouBanHref() {
		return douBanHref;
	}

	public void setDouBanHref(String douBanHref) {
		this.douBanHref = douBanHref;
	}

	public List<LibraryDetailListItemInfo> getList() {
		return list;
	}

	public void setList(List<LibraryDetailListItemInfo> list) {
		this.list = list;
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

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}

}
