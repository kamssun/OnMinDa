package com.newthread.android.clock;

public interface ITimeService {
	/**
	 * 从数据库依次取消所有已注册过的时间
	 */
	void cancleAllClock();

	/**
	 * 将数据库中所有时间全部注册
	 */
	void registAllClock();

	/**
	 * 取消已注册的时间任务
	 */
	void cancleTimeTask(TimeTask timeTask);

	/**
	 * 注册时间任务
	 */
	void registClock(TimeTask timeTask);

	/**
	 * 将时间任务写入数据库，注意并没有注册，到时间了不会启动
	 */
	void addTimeTaskInDB(TimeTask timeTask);

	/**
	 * 删除时间任务,注意到时间了还会启动，因为并没有取消
	 */
	void delTimeTaskInDB(TimeTask timeTask);

	/**
	 * 删除小于当前时间的时间任务
	 */
	void delPreTimeTask();
}
