package com.newthread.android.ui.labquery;

import java.io.Serializable;

public class LabDetail implements Serializable {
	private String WeekNum;// 周次
	private String TeacherName;// 老师姓名
	private String LabName;// 实验名称
	private String LabState;// 实验状态
	private String Week;// 星期
	private String Time;// 时段
	private String LabLocation;// 实验室地点
	private String Score;// 实验分数

	public String getWeekNum() {
		return WeekNum;
	}

	public void setWeekNum(String weekNum) {
		WeekNum = weekNum;
	}

	public String getTeacherName() {
		return TeacherName;
	}

	public void setTeacherName(String teacherName) {
		TeacherName = teacherName;
	}

	public String getLabName() {
		return LabName;
	}

	public void setLabName(String labName) {
		LabName = labName;
	}

	public String getLabState() {
		return LabState;
	}

	public void setLabState(String labState) {
		LabState = labState;
	}

	public String getWeek() {
		return Week;
	}

	public void setWeek(String week) {
		Week = week;
	}

	public String getTime() {
		return Time;
	}

	public void setTime(String time) {
		Time = time;
	}

	public String getLabLocation() {
		return LabLocation;
	}

	public void setLabLocation(String labLocation) {
		LabLocation = labLocation;
	}

	public String getScore() {
		return Score;
	}

	public void setScore(String score) {
		Score = score;
	}

}
