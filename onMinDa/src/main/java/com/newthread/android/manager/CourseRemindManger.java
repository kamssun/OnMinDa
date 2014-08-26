package com.newthread.android.manager;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Vibrator;
import com.newthread.android.R;
import com.newthread.android.activity.main.OnCampusActivity;
import com.newthread.android.bean.SingleCourseInfo;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2014/8/26.
 */
public class CourseRemindManger implements IRemindService<SingleCourseInfo> {

    private CourseRemindManger() {

    }

    private static class CourseRemindMangerHolder {
        private static CourseRemindManger instance = new CourseRemindManger();
    }

    public static CourseRemindManger getInstance() {
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
    public void openSeletedRemind(List<SingleCourseInfo> t) {

    }

    @Override
    public void closeSeletedRemind(List<SingleCourseInfo> t) {

    }

    @Override
    public void creatNotiforcation(SingleCourseInfo singleCourseInfo, Context context) {
        Notification notification = new Notification();
        notification.icon = R.drawable.notify;
        notification.when = System.currentTimeMillis();
        notification.sound = Uri.parse("android.resource://" + context.getPackageName() + File.separator + R.raw.order);
        notification.tickerText = "要上课啦！";
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        Intent intent = new Intent(context.getApplicationContext(), OnCampusActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(),0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        notification.contentIntent = pendingIntent;
        notification.setLatestEventInfo(context.getApplicationContext(), "人在民大",singleCourseInfo.getCourseName() , pendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        vibrate(context);
        notificationManager.notify(1, notification);
    }
    /**
     * 手机震动
     */
    private void vibrate(Context context) {
        try {
            Vibrator vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
            vibrator.vibrate(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
