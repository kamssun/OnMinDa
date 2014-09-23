package com.newthread.android.clock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.newthread.android.util.DatabaseManager;


public class TimeServiceImpl implements ITimeService {
    private final String DB_NAME = "OnMinDaClock.db";
    private DatabaseManager<TimeTask> dbManger;
    private Context context;
    private AlarmManager alarmMgr;

    public TimeServiceImpl(Context context) {
        super();
        this.context = context;
        inItDB(context);
        alarmMgr = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
    }

    /**
     * 用于过滤小于当前时间的任务
     *
     * @param times 需要过滤的时间任务列表
     * @return 返回大于当前时间的任务列表
     */
    private List<TimeTask> filterTime(List<TimeTask> times) {
        List<TimeTask> timeTemp = new ArrayList<TimeTask>();
        if (times != null) {
            for (int i = 0; i < times.size(); i++) {
                if (Long.valueOf(times.get(i).getTime()) > Long
                        .valueOf(getCurrentTime())) {
                    timeTemp.add(times.get(i));
                }
            }
            return timeTemp;
        }
        return null;
    }

    /**
     * @return 得到当前时间
     */
    private String getCurrentTime() {
        Date date = new Date();
        // format对象是用来以指定的时间格式格式化时间的
        SimpleDateFormat from = new SimpleDateFormat("yyyyMMddHHmm"); // 这里的格式可以自己设置
        // format()方法是用来格式化时间的方法
        String times = from.format(date);
        return times;
    }

    /**
     * @param timeTask
     * @return 得到 时间任务的longmills
     */
    private String getMills(TimeTask timeTask) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        try {
            long millionSeconds = sdf.parse(timeTask.getTime()).getTime();
            return millionSeconds + "";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从数据库依次取消所有已注册过的时间
     */
    @Override
    public void cancleAllClock() {
        List<TimeTask> timeTasks = dbManger.query();
        if (timeTasks.size() == 0) {
            return;
        }
        for (TimeTask timeTask : timeTasks) {
            cancleTimeTask(timeTask);
        }
    }

    /**
     * 将数据库中所有时间全部注册
     */
    @Override
    public void registAllClock() {
        List<TimeTask> timeTasks = dbManger.query();
        if (timeTasks.size() == 0) {
            return;
        }
        for (TimeTask timeTask : timeTasks) {
            registClock(timeTask);
        }
    }

    /**
     * 初始化数据库
     *
     * @param context
     */
    public void inItDB(Context context) {
        dbManger = new DatabaseManager<TimeTask>(context, TimeTask.class);
        dbManger.openTable("colock", DB_NAME);
    }

    /**
     * 取消已注册的时间任务
     */
    @Override
    public void cancleTimeTask(TimeTask timeTask) {
        List<TimeTask> timeTasks = dbManger.query(timeTask);
        if (timeTasks.isEmpty()) {
            Toast.makeText(context, "查询为空", Toast.LENGTH_LONG).show();
            return;
        }
        for (TimeTask timeTask2 : timeTasks) {
            try {
                PendingIntent pendIntent = creatCanclePIntent(timeTask2);
                // 与上面的intent匹配（filterEquals(intent)）的闹钟会被取消
                alarmMgr.cancel(pendIntent);
            } catch (NumberFormatException e) {
            }
        }
    }

    /**
     * @param timeTask
     * @return 创建一个可以取消时间任务的pendingIntent
     * @throws NumberFormatException
     */
    private PendingIntent creatCanclePIntent(TimeTask timeTask) {
        if (!timeTask.getType().equals("broadcast")) {
            Intent intent = new Intent(context, TimeTaskMeta.valueOf(timeTask.getTaskName()).getStartClass());
            if (timeTask.getType().equals("activity")) {
                PendingIntent pendIntent = PendingIntent.getActivity(context, Integer.parseInt(timeTask.getRequestCode()), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                return pendIntent;
            } else {
                PendingIntent pendIntent = PendingIntent.getService(context, Integer.parseInt(timeTask.getRequestCode()), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                return pendIntent;
            }
        } else {
            Intent intent = new Intent(TimeTaskMeta.valueOf(timeTask.getTaskName()).getAction());
            PendingIntent pendIntent = PendingIntent.getBroadcast(context, Integer.parseInt(timeTask.getRequestCode()), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            return pendIntent;
        }
    }

    /**
     * @param timeTask
     * @param i
     * @return 创建一个时间任务的pendingIntent
     */
    private PendingIntent createRigistPIntent(TimeTask timeTask, int i, Bundle bundle) {
        timeTask.setRequestCode(i + "");
        Intent _intent;
        if (timeTask.getType().equals("activity")) {
            _intent = new Intent(context, TimeTaskMeta.valueOf(timeTask.getTaskName()).getStartClass());
            _intent.putExtras(bundle);
            _intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pi = PendingIntent.getActivity(context, i, _intent, 0);
            return pi;
        } else if (timeTask.getType().equals("broadcast")) {
            _intent = new Intent(TimeTaskMeta.valueOf(timeTask.getTaskName()).getAction());
            _intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _intent.putExtras(bundle);
            PendingIntent pi = PendingIntent.getBroadcast(context, i, _intent, 0);
            return pi;
        } else if (timeTask.getType().equals("service")) {
            _intent = new Intent(context, TimeTaskMeta.valueOf(timeTask.getTaskName()).getStartClass());
            _intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _intent.putExtras(bundle);
            PendingIntent pi = PendingIntent.getService(context, i, _intent, 0);
            return pi;
        }
        return null;
    }


    /**
     * 注册时间任务
     */
    @Override
    public void registClock(TimeTask timeTask) {
        registClock(timeTask,null);
    }

    @Override
    public void registClock(TimeTask timeTask, Bundle bundle) {
        List<TimeTask> timeTasks = dbManger.query();
        PendingIntent pi = createRigistPIntent(timeTask, timeTasks.size(),bundle);
        if (timeTask.getRepetTimeMills() == null) {
            alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, Long.valueOf(getMills(timeTask)), pi);
        } else {
            alarmMgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, Long.valueOf(getMills(timeTask)), Long.valueOf(timeTask.getRepetTimeMills()), pi);
        }
        addTimeTaskInDB(timeTask);
    }



    /**
     * 将时间任务写入数据库，注意并没有注册，到时间了不会启动
     */
    @Override
    public synchronized void addTimeTaskInDB(TimeTask timeTask) {
        dbManger.add(timeTask);
    }

    /**
     * 删除时间任务,注意到时间了还会启动，因为并没有取消
     */
    @Override
    public synchronized void delTimeTaskInDB(TimeTask timeTask) {
        dbManger.delete(timeTask);
    }

    /**
     * 删除小于当前时间的时间任务
     */
    @Override
    public synchronized void delPreTimeTask() {
        List<TimeTask> timeTasks = filterTime(dbManger.query());
        dbManger.cleanTable();
        dbManger.add(timeTasks);
    }

}
