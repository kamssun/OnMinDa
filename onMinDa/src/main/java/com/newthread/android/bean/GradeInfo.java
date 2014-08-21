package com.newthread.android.bean;

import java.io.Serializable;


/**
 * 课程成绩信息
 * @author Su
 */
public class GradeInfo implements Serializable {
	/**
	 */
	private static final long serialVersionUID = 1L;

	private String id;			// 在数据库中的id 
	
	private String year;		// 学年数, eg: 2012-2013
	private int semesterNum;	// 学期数, eg: 第1学期-> 1
	private String courseName;	// 课程名
	private String courseID;	// 课程号
	private String courseType;	// 课程类别
	private String courseProperty;	// 课程性质： 选修 or 必修 
	private float courseCredit;	// 学分数
	private String courseGrade;	// 分数
	private String studyType;	// 修读方式: eg: 初修
	private float GPA;			// 绩点
	
	private boolean isSelected;	// for: 多选状态使用
	
	public GradeInfo() {
		super();
		this.isSelected = true;
	}
	
	public boolean isSelected() {
		return isSelected;
	}

	public String getCourseID() {
		return courseID;
	}

	public void setCourseID(String courseID) {
		this.courseID = courseID;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public int getSemesterNum() {
		return semesterNum;
	}

	public void setSemesterNum(int semesterNum) {
		this.semesterNum = semesterNum;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

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

	public float getCourseCredit() {
		return courseCredit;
	}

	public void setCourseCredit(float courseCredit) {
		this.courseCredit = courseCredit;
	}

	public String getCourseGrade() {
		return courseGrade;
	}

	public void setCourseGrade(String courseGrade) {
		this.courseGrade = courseGrade;
	}

	public String getStudyType() {
		return studyType;
	}

	public void setStudyType(String studyType) {
		this.studyType = studyType;
	}

	public float getGPA() {
		return GPA;
	}

	public void setGPA(float gPA) {
		GPA = gPA;
	}
}
