package com.newthread.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.newthread.android.activity.main.MyApplication;
import com.newthread.android.bean.SingleCourseInfo;
import com.newthread.android.manager.CourseRemindManger;

/**
 * Created by Administrator on 2014/8/26.
 */
public class CouserReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.newthread.android.action.CourserRemind")) {
            SingleCourseInfo singleCourseInfo = MyApplication.getInstance().getThing(intent.getExtras().getString("name"));
            CourseRemindManger.getInstance(context).creatNotiforcation(singleCourseInfo);
        }
    }
}
