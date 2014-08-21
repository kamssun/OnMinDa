package com.newthread.android.bean;

/**
 * 成绩查询Post参数信息
 * @author sushun
 */
public class GradeQueryVo {
	private String year;		// 学年
	private String semester;	// 学期: 1/2
	private String courseType;	// 课程类别
	private String courseProperty;	// 课程性质
	
	private String account;
	private String password;
	
	public String getCourseType() {
		return courseType;
	}
	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}
	public String getCourseProperty() {
		return courseProperty;
	}
	public void setCourseProperty(String courseProperty) {
		this.courseProperty = courseProperty;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getSemester() {
		return semester;
	}
	public void setSemester(String semester) {
		this.semester = semester;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
