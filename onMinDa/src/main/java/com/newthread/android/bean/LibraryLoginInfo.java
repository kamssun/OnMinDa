package com.newthread.android.bean;

import java.util.List;

public class LibraryLoginInfo {
	private PersonalInfo personalInfo;	// 个人信息
	
	private String goingOverTimeNum; 	// 5天内将要超期的数目
	private String overTimeNum;		 	// 已超期的数目
	
	private List<LibraryCurrentBorrow> currnetBorrowList;	// 当前借阅
	
	public LibraryLoginInfo() {
		this.personalInfo = new PersonalInfo();
	}
	
	public PersonalInfo getPersonalInfo() {
		return personalInfo;
	}
	public void setPersonalInfo(PersonalInfo personalInfo) {
		this.personalInfo = personalInfo;
	}
	public String getGoingOverTimeNum() {
		return goingOverTimeNum;
	}
	public void setGoingOverTimeNum(String goingOverTimeNum) {
		this.goingOverTimeNum = goingOverTimeNum;
	}
	public String getOverTimeNum() {
		return overTimeNum;
	}
	public void setOverTimeNum(String overTimeNum) {
		this.overTimeNum = overTimeNum;
	}
	public List<LibraryCurrentBorrow> getCurrnetBorrowList() {
		return currnetBorrowList;
	}
	public void setCurrnetBorrowList(List<LibraryCurrentBorrow> currnetBorrowList) {
		this.currnetBorrowList = currnetBorrowList;
	}
	
	
}
