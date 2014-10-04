package com.newthread.android.bean;

import java.io.Serializable;

/**
 * 每节课的课程信息
 * @author _foumnder
 *
 */
public class SingleCourseInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;				// 数据库id
	
	private boolean isHaveCourse;	// 是否有课
	
	private String courseName;		// 课程名
	private String teacherName;		// 老师名
	private String sustainTime;		// 课程持续时间
	private String classromNum;		// 教室号
	private String numOfDay;		// 第几节课
	private int numOfWeek;			// 周几的课
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getNumOfWeek() {
		return numOfWeek;
	}
	public void setNumOfWeek(int numOfWeek) {
		this.numOfWeek = numOfWeek;
	}
	public boolean isHaveCourse() {
		return isHaveCourse;
	}
	public void setHaveCourse(boolean isHaveCourse) {
		this.isHaveCourse = isHaveCourse;
	}
	
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getSustainTime() {
		return sustainTime;
	}
	public void setSustainTime(String sustainTime) {
		this.sustainTime = sustainTime;
	}
	public String getClassromNum() {
		return classromNum;
	}
	public void setClassromNum(String classromNum) {
		this.classromNum = classromNum;
	}
	public String getNumOfDay() {
		return numOfDay;
	}
	public void setNumOfDay(String numOfDay) {
		this.numOfDay = numOfDay;
	}

    @Override
    public String toString() {
        return "SingleCourseInfo{" +
                "id='" + id + '\'' +
                ", isHaveCourse=" + isHaveCourse +
                ", courseName='" + courseName + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", sustainTime='" + sustainTime + '\'' +
                ", classromNum='" + classromNum + '\'' +
                ", numOfDay='" + numOfDay + '\'' +
                ", numOfWeek=" + numOfWeek +
                '}';
    }
}
