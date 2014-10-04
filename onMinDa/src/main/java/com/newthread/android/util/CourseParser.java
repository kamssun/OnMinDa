package com.newthread.android.util;

import android.util.Log;
import com.newthread.android.bean.SingleCourseInfo;

/**
 * Created by 翌日黄昏 on 2014/10/2.
 */
public class CourseParser {
    private static final String TAG = "CourseParser";

    public static SingleCourseInfo parser(String cell) {
        SingleCourseInfo singleCourse = new SingleCourseInfo();
        if (!cell.equals(" ")) {
            // 有课
            singleCourse.setHaveCourse(true);
            String[] itemsT = cell.split("<hr />");
            if (itemsT.length == 1) {
                //非单双周轮上课程
                Log.v(TAG, "非单双周轮上课程");
                String[] items = itemsT[0].split("<br />");
                // 教师名
                singleCourse.setTeacherName(items[1].replace(" ", ""));
                singleCourse.setClassromNum(items[2]);
                items = items[0].split(" ");
                // 课程持续周数
                singleCourse.setSustainTime(items[items.length - 2]);
                int index = items.length - 2;
                String courseName = "";
                while (--index >= 0) {
                    courseName = items[index] + courseName;
                }
                singleCourse.setCourseName(courseName);
            } else if (itemsT.length == 2) {
                //单双周轮上课程
                Log.v(TAG, "单双周轮上课程");
                String[] items0 = itemsT[0].split("<br />");
                String[] items1 = itemsT[1].split("<br />");

                // 教师名
                singleCourse.setTeacherName(items0[1].replace(" ", "") + "/\n" + items1[1].replace(" ", ""));
                singleCourse.setClassromNum(items0[2] + "/\n" + items1[2]);

                items0 = items0[0].split(" ");
                items1 = items1[0].split(" ");
                // 课程持续周数
                singleCourse.setSustainTime(items0[items0.length - 2] + "/\n" + items1[items1.length - 2]);
                int index0 = items0.length - 1;
                int index1 = items1.length - 1;
                String courseName0 = "";
                String courseName1 = "";
                while (--index0 >= 0) {
                    courseName0 = items0[index0] + courseName0;
                }
                while (--index1 >= 0) {
                    courseName1 = items1[index1] + courseName1;
                }
                singleCourse.setCourseName(courseName0 + "/\n" + courseName1);
            }else {
                //不支持的课程类型
            }

//            while (cell.charAt(0) == ' ') cell = cell.substring(1);
//            Log.v(TAG, cell);
//            String[] items = cell.split(" ");
//            Log.v(TAG, items.length + "");
//            if (items.length == 6) {
//                singleCourse.setCourseName(items[0]);
//                // 课程持续周数
//                singleCourse.setSustainTime(items[1].substring(1, items[1].length()));
//                // 教师名
//                singleCourse.setTeacherName(items[3]);
//                singleCourse.setClassromNum(items[5]);
//            } else if(items.length == 7) {
//                singleCourse.setCourseName(items[0] + items[1]);
//                // 课程持续周数
//                singleCourse.setSustainTime(items[2].substring(1, items[2].length()));
//                // 教师名
//                singleCourse.setTeacherName(items[4]);
//                singleCourse.setClassromNum(items[6]);
//            } else if(items.length == 8) {
//                singleCourse.setCourseName(items[0] + items[1] + "/" + items[4] + items[5]);
//                // 课程持续周数
//                singleCourse.setSustainTime(items[1].substring(1, items[1].length()));
//                // 教师名
//                singleCourse.setTeacherName(items[3] + "/" + items[7]);
//                singleCourse.setClassromNum("No Classroom");
//            } else if (items.length < 6 && items.length > 0) {
//                singleCourse.setCourseName(items[0]);
//                // 课程持续周数
//                singleCourse.setSustainTime(items[1].substring(1, items[1].length()));
//                // 教师名
//                singleCourse.setTeacherName(items[3]);
//                singleCourse.setClassromNum("No Classroom");
//            } else {
//                //不支持的课程类型
//            }
        } else {
            // 无课
            singleCourse.setHaveCourse(false);
        }
        return singleCourse;
    }
}
