package com.newthread.android.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	private static final int DATEBASE_VERSION = 1;
	private static final String DB_NAME = "OnMinDa.db"; // 数据库名

	public static final String TABLE_PERSONAL_INFO = "personal_info"; // 个人信息
	public static final String TABLE_BOOK_COLLECT = "book_collect"; // 图书收藏
	public static final String TABLE_NEWS_LIST = "news_list"; // 新闻列表
	public static final String TABLE_CURRENT_BORROW = "current_borrow"; // 图书当前借阅
	public static final String TABLE_COURSE_CHART = "course_chart"; // 课程表
	public static final String TABLE_LAB_CHART = "Lab";// 实验

	public DBHelper(Context context) {
		super(context, DB_NAME, null, DATEBASE_VERSION);
	}

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// ///////////// 个人信息 //////////////
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_PERSONAL_INFO
				+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ " name VARCHAR(16)," // 书名
				+ " gender VARCHAR(16)," // 性别
				+ " college VARCHAR(64)," // 学院&专业
				+ " borrow_num VARCHAR(16)," // 累计借书数目
				+ " student_id VARCHAR(16)," // 学号
				+ " bar_code_id VARCHAR(32)," // 条码号
				+ " add_time DATETIME," // 存入时间
				+ " type VARCHAR(10)" // 类型
				+ ")");

		// ///////////// 图书收藏 //////////////
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_BOOK_COLLECT
				+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ " book_name VARCHAR(64)," // 书名
				+ " publisher VARCHAR(64)," // 出版商
				+ " auther VARCHAR(64)," // 作者
				+ " book_id VARCHAR(64)," // 索书号
				+ " place VARCHAR(64)," // 馆藏地点
				+ " url VARCHAR(128)," // 链接
				+ " add_time DATETIME," // 存入时间
				+ " type VARCHAR(10)" // 类型
				+ ")");

		// ///////////// 图书当前借阅 //////////////
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_CURRENT_BORROW
				+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ " book_name VARCHAR(64)," // 书名
				+ " over_time DATETIME," // 超期时间
				+ " add_time DATETIME," // 存入时间
				+ " can_renew INTEGER," // 是否可续借 0:否， 1:是
				+ " type VARCHAR(10)" // 类型
				+ ")");

		// //////////// 新闻列表 //////////////
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NEWS_LIST
				+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ " title VARCHAR(50)," + " digest VARCHAR(100),"
				+ " time VARCHAR(50)," + " href VARCHAR(100),"
				+ " add_time DATETIME," + " type VARCHAR(10)" + ")");

		// //////////// 课程表 //////////////
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_COURSE_CHART
				+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ " course_name VARCHAR(64)," // 课程名
				+ " day_of_week INTEGER," // 周几的课
				+ " teacher VARCHAR(16)," // 代课老师
				+ " place VARCHAR(16)," // 上课地点
				+ " course_time VARCHAR(32)," // 上课时间
				+ " duration_week VARCHAR(32)," // 持续周数
				+ " have_course INTEGER," // 是否有课 0:否， 1:是
				+ " add_time DATETIME," + " type VARCHAR(10)" + ")");
		// ////////////实验表 //////////////
		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ TABLE_LAB_CHART
				+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT, LabName String, WeekNum String,Week String,Time String,LabLocation String,TeacherName String,Score String)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
