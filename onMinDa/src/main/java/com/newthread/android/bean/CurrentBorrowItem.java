package com.newthread.android.bean;

/**
 * 当前借阅列表实体类
 * @author Su
 */
public class CurrentBorrowItem {
	private String id;
	
	private String bookName;
	private String overTime;
	private String renNewBook_str1;
	private String renNewBook_str2;
	private boolean canRenew;
	
	public boolean isCanRenew() {
		return canRenew;
	}
	public void setCanRenew(boolean canRenew) {
		this.canRenew = canRenew;
	}
	public String getId() {
		return id;
	}
	public String getRenNewBook_str1() {
		return renNewBook_str1;
	}
	public void setRenNewBook_str1(String renNewBook_str1) {
		this.renNewBook_str1 = renNewBook_str1;
	}
	public String getRenNewBook_str2() {
		return renNewBook_str2;
	}
	public void setRenNewBook_str2(String renNewBook_str2) {
		this.renNewBook_str2 = renNewBook_str2;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public String getOverTime() {
		return overTime;
	}
	public void setOverTime(String overTime) {
		this.overTime = overTime;
	}
}
