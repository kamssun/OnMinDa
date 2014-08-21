package com.newthread.android.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 每天的课程信息
 *
 */
public class EverydayCourse implements Serializable {
	private static final long serialVersionUID = 1L;
	private ArrayList<SingleCourseInfo> dayOfWeek;
	
	public EverydayCourse() {
		dayOfWeek = new ArrayList<SingleCourseInfo>();
	}

	public EverydayCourse(ArrayList<SingleCourseInfo> dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public ArrayList<SingleCourseInfo> getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(ArrayList<SingleCourseInfo> dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
}
