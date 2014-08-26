package com.newthread.android.manager;


/**
 * Created by Administrator on 2014/8/26.
 */
public class CourseRemindManger implements IRemindService {

    private CourseRemindManger() {

    }

    private static class CourseRemindMangerHolder {
        private static CourseRemindManger instance = new CourseRemindManger();
    }

    public CourseRemindManger getInstance() {
        return CourseRemindMangerHolder.instance;
    }

    @Override
    public void init() {

    }

    @Override
    public void openAllRemind() {

    }

    @Override
    public void closeALlRemind() {

    }

    @Override
    public void openSeletedRemind() {

    }

    @Override
    public void closeSeletedRemind() {

    }

}
