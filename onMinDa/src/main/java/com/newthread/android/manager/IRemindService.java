package com.newthread.android.manager;

import android.content.Context;
import com.newthread.android.bean.SingleCourseInfo;

import java.util.List;

/**
 * Created by Administrator on 2014/8/26.
 */
public interface IRemindService<T> {
    void openAllRemind();
    void closeALlRemind();
    void openRemind(T t);
    void closeRemind(T t);
    void openSeletedRemind(List<T> t);
    void closeSeletedRemind(List<T> t);
    void creatNotiforcation(T t);
}
