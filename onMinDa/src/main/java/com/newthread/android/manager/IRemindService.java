package com.newthread.android.manager;

import android.content.Context;

import java.util.List;

/**
 * Created by Administrator on 2014/8/26.
 */
public interface IRemindService<T> {
    void init();

    void openAllRemind();

    void closeALlRemind();

    void openSeletedRemind(List<T> t);

    void closeSeletedRemind(List<T> t);

    void creatNotiforcation(T t,Context context);
}
