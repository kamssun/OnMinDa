package com.newthread.android.manager;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import com.newthread.android.R;
import com.newthread.android.activity.main.MyApplication;
import com.newthread.android.activity.main.OnCampusActivity;
import com.newthread.android.bean.SingleCourseInfo;
import com.newthread.android.clock.TimeManager;
import com.newthread.android.clock.TimeTask;
import com.newthread.android.util.TimeUtil;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2014/8/26.
 */
public class CourseRemindManger implements IRemindService<SingleCourseInfo> {
    private Context context;
    private static CourseRemindManger instance;

    private CourseRemindManger(Context context) {
        this.context = context;
    }

    public static CourseRemindManger getInstance(Context context) {
        synchronized (CourseRemindManger.class) {
            if (instance == null) {
                instance = new CourseRemindManger(context);
            }
        }
        return instance;
    }


    @Override
    public void openAllRemind() {

    }

    @Override
    public void closeALlRemind() {

    }

    @Override
    public void openRemind(SingleCourseInfo singleCourseInfo) {
        String name = singleCourseInfo.getCourseName();
        MyApplication.getInstance().putThing(name, singleCourseInfo);
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        String time =TimeUtil.getTimeFromCourse(singleCourseInfo,15);
        TimeManager.getInstance(context).registClock(new TimeTask(time, "couserBroadcast", "broadcast"), bundle);
    }

    @Override
    public void closeRemind(SingleCourseInfo singleCourseInfo) {

    }

    @Override
    public void openSeletedRemind(List<SingleCourseInfo> t) {

    }

    @Override
    public void closeSeletedRemind(List<SingleCourseInfo> t) {

    }

    @Override
    public void creatNotiforcation(SingleCourseInfo singleCourseInfo) {
        Notification notification = new Notification();
        notification.icon = R.drawable.notify;
        notification.when = System.currentTimeMillis();
        notification.sound = Uri.parse("android.resource://" + context.getPackageName() + File.separator + R.raw.order);
        notification.tickerText = "要上课啦！";
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        Intent intent = new Intent(context, OnCampusActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        notification.contentIntent = pendingIntent;
        notification.setLatestEventInfo(context, "人在民大", singleCourseInfo.getCourseName(), pendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        vibrate();
        notificationManager.notify(1, notification);
    }

    /**
     * 手机震动
     */
    private void vibrate() {
        try {
            Vibrator vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
            vibrator.vibrate(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
