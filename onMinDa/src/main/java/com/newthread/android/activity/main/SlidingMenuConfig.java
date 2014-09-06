package com.newthread.android.activity.main;

import com.newthread.android.ui.coursechart.CourseChartLoginActivity;
import com.newthread.android.ui.exam.ExamArrangeActivity;
import com.newthread.android.ui.grade.GradeSelect;
import com.newthread.android.ui.labquery.LabLogin_activity;
import com.newthread.android.ui.library.LibraryActivity;
import com.newthread.android.ui.library.LibraryCollectActivity;
import com.newthread.android.ui.library.LibraryCurrentBorrowActivity;
import com.newthread.android.ui.news.NewsActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jindongping on 14-9-6.
 */
public class SlidingMenuConfig {

    private static SlidingMenuConfig instance = new SlidingMenuConfig();
    private List< ItemMenu> leftList;
    private List< ItemMenu> rightList;

    private SlidingMenuConfig() {
        leftList = new ArrayList<>();
        setLeftListView(leftList);
        rightList = new ArrayList<>();
        setRightListView(rightList);
    }

    private void setRightListView(List< ItemMenu>rightList) {
        rightList.add(new ItemMenu("我的账号",MyAccountActivity.class));
        rightList.add(new ItemMenu("图书收藏",LibraryCollectActivity.class));
        rightList.add(new ItemMenu("当前借阅",LibraryCurrentBorrowActivity.class));
        rightList.add(new ItemMenu("成绩查询",GradeSelect.class));
        rightList.add(new ItemMenu("实验查询",LabLogin_activity.class));
        rightList.add(new ItemMenu("考试安排",ExamArrangeActivity.class));
        rightList.add(new ItemMenu("设置",SettingActivity.class));
    }


    private void setLeftListView(List< ItemMenu> leftList) {
     leftList.add(new ItemMenu("图书馆",LibraryActivity.class));
     leftList.add(new ItemMenu("课程表",CourseChartLoginActivity.class));
     leftList.add(new ItemMenu("校园新闻", NewsActivity.class));
     leftList.add(new ItemMenu("校园网连接",SchoolNetActivity.class));
    }


    public static SlidingMenuConfig getInstance() {
        return instance;
    }

    public List< ItemMenu> getLeftList() {
        return leftList;
    }

    public List< ItemMenu>getRightList() {
        return rightList;
    }




    public class ItemMenu {
        private String title;
        private Class clazz;

        private ItemMenu(String title, Class clazz) {
            this.title = title;
            this.clazz = clazz;
        }

        public String getTitle() {
            return title;
        }

        public Class getClazz() {
            return clazz;
        }
    }
}
