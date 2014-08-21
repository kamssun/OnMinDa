package com.newthread.android.bean;

public class LibraryCurrentBorrow {
	private String title;			// 书籍名
	private String overTime;		// 借阅时间
	private boolean canRenew;		// 是否可以续借
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public boolean isCanRenew() {
		return canRenew;
	}
	public void setCanRenew(boolean canRenew) {
		this.canRenew = canRenew;
	}
	public String getOverTime() {
		return overTime;
	}
	public void setOverTime(String overTime) {
		this.overTime = overTime;
	}
}
