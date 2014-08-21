package com.newthread.android.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.newthread.android.bean.BookCollectItem;
import com.newthread.android.bean.CurrentBorrowItem;
import com.newthread.android.bean.EverydayCourse;
import com.newthread.android.bean.LibraryCurrentBorrow;
import com.newthread.android.bean.NewsListItem;
import com.newthread.android.bean.PersonalInfo;
import com.newthread.android.service.DBBookCollectManager;
import com.newthread.android.service.DBBookCurrentBorrowManager;
import com.newthread.android.service.DBCourseChartManager;
import com.newthread.android.service.DBLabManager;
import com.newthread.android.service.DBNewsListManager;
import com.newthread.android.service.DBPersonalInfoManager;
import com.newthread.android.ui.labquery.LabDetail;

/**
 * 数据库管理
 * 
 * @author sushun
 */
public class AndroidDB {
	// ////////////// 个人信息 ////////////////
	// 添加
	public static void addPersonalInfo(Context context, PersonalInfo item) {
		DBPersonalInfoManager dbm = new DBPersonalInfoManager(context);
		dbm.deleteAllData();
		dbm.add(item);
	}

	// 删除
	public static void deletePersonalInfo(Context context, PersonalInfo item) {
		DBPersonalInfoManager dbm = new DBPersonalInfoManager(context);
		dbm.deleteByID(item.getId());
	}

	// 查询
	public static PersonalInfo queryPersonalInfo(Context context) {
		DBPersonalInfoManager dbm = new DBPersonalInfoManager(context);
		return dbm.query();
	}

	// ////////////// 图书馆 ////////////////
	// 添加图书收藏信息到数据库
	public static void addBookCollect(Context context, BookCollectItem item) {
		DBBookCollectManager dbm = new DBBookCollectManager(context);
		dbm.add(item);
	}

	// 删除图书收藏信息
	public static void deleteBookCollect(Context context, BookCollectItem item) {
		DBBookCollectManager dbm = new DBBookCollectManager(context);
		dbm.deleteByID(item.getID());
	}

	// 查询所有图书收藏信息
	public static ArrayList<BookCollectItem> query(Context context) {
		DBBookCollectManager dbm = new DBBookCollectManager(context);
		return dbm.query();
	}

	// 判断图书是否存在，根据索书号
	public static boolean haveCollected(Context context, String book_id) {
		DBBookCollectManager dbm = new DBBookCollectManager(context);
		return dbm.haveCollected(book_id);
	}

	// ////////////// 当前借阅 ////////////////
	// 添加当前借阅图书信息
	public static void addCurrentBorrowList(Context context,
			ArrayList<CurrentBorrowItem> items) {
		DBBookCurrentBorrowManager dbm = new DBBookCurrentBorrowManager(context);
		dbm.add(items);
	}

	// 添加当前借阅图书信息
	public static void addCurrentBorrow(Context context, CurrentBorrowItem item) {
		DBBookCurrentBorrowManager dbm = new DBBookCurrentBorrowManager(context);
		dbm.add(item);
	}

	// 删除
	public static void deleteCurrentBorrow(Context context,
			CurrentBorrowItem item) {
		DBBookCurrentBorrowManager dbm = new DBBookCurrentBorrowManager(context);
		dbm.deleteByID(item.getId());
	}

	// 删除全部
	public static void deleteCurrentBorrowAll(Context context) {
		DBBookCurrentBorrowManager dbm = new DBBookCurrentBorrowManager(context);
		dbm.deleteAllData();
	}

	// 查询所有
	public static ArrayList<CurrentBorrowItem> queryCurrentBorrow(
			Context context) {
		DBBookCurrentBorrowManager dbm = new DBBookCurrentBorrowManager(context);
		return dbm.query();
	}

	// ////////////////////// 新闻列表 /////////////////////
	// 添加新闻信息列表，先清除，再添加
	public static void addNewsList(Context context, List<NewsListItem> list,
			String type) {
		DBNewsListManager dbm = new DBNewsListManager(context);

		dbm.deleteByType(type);

		dbm.add(list);
	}

	// 获取全部新闻
	public static ArrayList<NewsListItem> getNewsList(Context context) {
		DBNewsListManager dbm = new DBNewsListManager(context);
		return dbm.query();
	}

	// 根据新闻类型获取新闻
	public static ArrayList<NewsListItem> getNewsListByType(Context context,
			String type) {
		DBNewsListManager dbm = new DBNewsListManager(context);
		return dbm.queryByType(type);
	}

	// ////////////////////// 课程表 /////////////////////
	// 添加，先清除，再添加
	public static void addCourse(Context context, List<EverydayCourse> list) {
		DBCourseChartManager dbm = new DBCourseChartManager(context);

		dbm.deleteAllData();

		for (int i = 0; i < list.size(); i++) {
			dbm.add(list.get(i).getDayOfWeek());
		}
		dbm.getDb().close();
	}

	// 添加选座记录
	public static void addLab(Context context, LabDetail item) {
		DBLabManager dbm = new DBLabManager(context);
		dbm.add(item);
	}

	// 查询所有实验数据
	public static ArrayList<LabDetail> queryLab(Context context) {
		DBLabManager dbm = new DBLabManager(context);
		return dbm.query();
	}

	// 判断某个实验是否在数据库已存在
	public static boolean querysomeone(Context context, String floor,
			String seat) {
		DBLabManager dbm = new DBLabManager(context);
		// return dbm.haveCollected(floor, seat);
		return false;
	}

	// 删除全部的数据
	public static void deleteLab(Context context) {
		DBLabManager dbm = new DBLabManager(context);
		dbm.deleteAllData();
	}

	// 获取全部
	public static ArrayList<EverydayCourse> getCourse(Context context) {
		DBCourseChartManager dbm = new DBCourseChartManager(context);
		return dbm.query();
	}
}
