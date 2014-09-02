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
import com.newthread.android.activity.main.OnCampusActivity;
import com.newthread.android.bean.CurrentBorrowItem;
import com.newthread.android.clock.TimeManager;
import com.newthread.android.clock.TimeTask;
import com.newthread.android.ui.library.LibraryCurrentBorrowActivity;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2014/8/26.
 */
public class LibraryBookRemindManger implements IRemindService<CurrentBorrowItem> {
    private Context context;

    private static LibraryBookRemindManger instance;

    private LibraryBookRemindManger(Context context) {
        this.context = context;
    }

    public static LibraryBookRemindManger getInstance(Context context) {
        synchronized (LibraryBookRemindManger.class) {
            if (instance == null) {
                instance = new LibraryBookRemindManger(context);
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
    public void openRemind(CurrentBorrowItem currentBorrowItem) {
    }

    @Override
    public void closeRemind(CurrentBorrowItem currentBorrowItem) {

    }

    @Override
    public void openSeletedRemind(List<CurrentBorrowItem> t) {

    }

    @Override
    public void closeSeletedRemind(List<CurrentBorrowItem> t) {

    }

    @Override
    public void creatNotiforcation(CurrentBorrowItem currentBorrowItem) {
        Notification notification = new Notification();
        notification.icon = R.drawable.notify;
        notification.when = System.currentTimeMillis();
        notification.sound = Uri.parse("android.resource://" + context.getPackageName() + File.separator + R.raw.order);
        notification.tickerText = "图书超期提醒";
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        Intent intent = new Intent(context.getApplicationContext(), LibraryCurrentBorrowActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        notification.contentIntent = pendingIntent;
        notification.setLatestEventInfo(context.getApplicationContext(), "人在民大", currentBorrowItem.getBookName() + "还有3天超期", pendingIntent);

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
