package com.newthread.android.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 每周的课程信息
 * @author _foumnder
 *
 */
public class WeekCourseInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<EverydayCourse> weekCourse;
	
	public WeekCourseInfo() {
		// 假设每周7有天课
		weekCourse = new ArrayList<EverydayCourse>(7);
	}
	
	public WeekCourseInfo(ArrayList<EverydayCourse> weekCourse) {
		// 假设每周7有天课
		this.weekCourse = weekCourse;
	}

	public ArrayList<EverydayCourse> getWeekCourse() {
		return weekCourse;
	}

	public void setWeekCourse(ArrayList<EverydayCourse> weekCourse) {
		this.weekCourse = weekCourse;
	}
	
	
}
