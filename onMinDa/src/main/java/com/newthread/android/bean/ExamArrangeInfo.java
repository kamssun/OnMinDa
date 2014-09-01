package com.newthread.android.bean;

/**
 * Created by jindongping on 14-9-1.
 */
public class ExamArrangeInfo {
    private String state;
    private String courseId;
    private String courseName;
    private String courseGrade;
    private String ExamAdress;

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

    public String getExamAdress() {
        return ExamAdress;
    }

    public void setExamAdress(String examAdress) {
        ExamAdress = examAdress;
    }
}
