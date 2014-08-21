package com.newthread.android.bean;

public class PersonalInfo {
	private String id;
	
	private String name;	// 姓名
	private String gender;	// 性别
	private String sid;  	// 学号
	private String bar;		// 借阅证条码
	private String college; // 学院&专业
	private String borrowNum;// 图书历史借阅数量
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBorrowNum() {
		return borrowNum;
	}
	public void setBorrowNum(String borrowNum) {
		this.borrowNum = borrowNum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getBar() {
		return bar;
	}
	public void setBar(String bar) {
		this.bar = bar;
	}
	public String getCollege() {
		return college;
	}
	public void setCollege(String college) {
		this.college = college;
	}
	
	
}
