package com.newthread.android.bean;

import org.kymjs.aframe.database.annotate.Id;

/**
 * Created by jindongping on 14-9-1.
 */
public class ExamArrangeInfo{
    @Id
    private int id ;
    private String state;//考试编排状态
    private String courseId;//课程id
    private String courseName;//课程名
    private String courseGrade;//学分
    private String courseTeacher;//课程老师
    private String courseType;//课程性质
    private String examAddress;//考试地址
    private String examTime;//考试时间
    private String examType;//考试类型
    private String examModle;//考试方式
    private String finishSate;//考试是否结束
    private String seatNum;//座位号

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExamModle() {
        return examModle;
    }

    public void setExamModle(String examModle) {
        this.examModle = examModle;
    }

    public String getFinishSate() {
        return finishSate;
    }

    public void setFinishSate(String finishSate) {
        this.finishSate = finishSate;
    }

    public String getCourseTeacher() {
        return courseTeacher;
    }

    public void setCourseTeacher(String courseTeacher) {
        this.courseTeacher = courseTeacher;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public String getExamTime() {
        return examTime;
    }

    public void setExamTime(String examTime) {
        this.examTime = examTime;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public String getSeatNum() {
        return seatNum;
    }

    public void setSeatNum(String seatNum) {
        this.seatNum = seatNum;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseGrade() {
        return courseGrade;
    }

    public void setCourseGrade(String courseGrade) {
        this.courseGrade = courseGrade;
    }

    public String getExamAddress() {
        return examAddress;
    }

    public void setExamAddress(String examAddress) {
        this.examAddress = examAddress;
    }

    @Override
    public String toString() {
        return "ExamArrangeInfo{" +
                "state='" + state + '\'' +
                ", courseId='" + courseId + '\'' +
                ", courseName='" + courseName + '\'' +
                ", courseGrade='" + courseGrade + '\'' +
                ", courseTeacher='" + courseTeacher + '\'' +
                ", courseType='" + courseType + '\'' +
                ", examAddress='" + examAddress + '\'' +
                ", examTime='" + examTime + '\'' +
                ", examType='" + examType + '\'' +
                ", examModle='" + examModle + '\'' +
                ", finishSate='" + finishSate + '\'' +
                ", seatNum='" + seatNum + '\'' +
                '}';
    }
}
