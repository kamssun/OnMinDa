package com.newthread.android.clock;

import android.content.Context;
import android.os.Bundle;

public class TimeManager {
	private ITimeService timeService;
	private static TimeManager instace = null;

	private TimeManager(Context context) {
		timeService = new TimeServiceImpl(context);
	}

	public static TimeManager getInstance(Context context) {
		if (instace == null) {
			return new TimeManager(context);
		}
		return instace;
	}

	/**
	 * 将时间任务写入数据库，注意并没有注册，到时间了不会启动
	 */
	public void addTimeTaskInDB(TimeTask timeTask) {
		timeService.addTimeTaskInDB(timeTask);
	}

	/**
	 * 删除时间任务,注意到时间了还会启动，因为并没有取消
	 */
	public void delTimeTaskInDB(TimeTask timeTask) {
		timeService.delTimeTaskInDB(timeTask);
	}

	/**
	 * 取消已注册的时间任务
	 */
	public void cancleTimeTask(TimeTask timeTask) {
		timeService.cancleTimeTask(timeTask);
	}

	public void cancleAllClock() {
		timeService.cancleAllClock();
	}

	/**
	 * 将数据库中所有时间全部注册
	 */
	public void registAllClock() {
		timeService.registAllClock();
	}

	/**
	 * 注册时间任务
	 */
	public void registClock(TimeTask timeTask) {
		timeService.registClock(timeTask);
	}
	public void registClock(TimeTask timeTask,Bundle bundle) {
		timeService.registClock(timeTask,bundle);
	}


	/**
	 * 删除小于当前时间的时间任务
	 */
	public void delPreTimeTask() {
		timeService.delPreTimeTask();
	}
}
