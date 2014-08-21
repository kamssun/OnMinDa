package com.newthread.android.service;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.newthread.android.bean.BookCollectItem;
import com.newthread.android.bean.EverydayCourse;
import com.newthread.android.bean.SingleCourseInfo;
import com.newthread.android.util.DBHelper;
import com.newthread.android.util.TimeUtil;

public class DBCourseChartManager {
	private DBHelper helper;
	private SQLiteDatabase db;
	
	public SQLiteDatabase getDb() {
		return db;
	}


	public static final String DEFAULT_TYPE = "1";
	public static final String TABLE_COURSE_CHART = "course_chart"; 	   	// 课程表
	
	public DBCourseChartManager(Context context) {
		helper = new DBHelper(context);
		db = helper.getWritableDatabase();
	}
	
	/////////////// ADD ///////////////////////////////////
	// 添加单条数据
	public void add(SingleCourseInfo item) {
		try {
			db.execSQL("INSERT INTO " + TABLE_COURSE_CHART + " VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
					new Object[] { item.getCourseName(), item.getNumOfWeek(), item.getTeacherName(), item.getClassromNum(), 
									item.getNumOfDay(), item.getSustainTime(), item.isHaveCourse() ? 1 : 0, TimeUtil.getCurrentTime(), DEFAULT_TYPE });
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}

	// 添加多条数据
	public void add(List<SingleCourseInfo> items) {
		db.beginTransaction();
		try {
			for (SingleCourseInfo item : items) {
				db.execSQL("INSERT INTO " + TABLE_COURSE_CHART + " VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
						new Object[] { item.getCourseName(), item.getNumOfWeek(), item.getTeacherName(), item.getClassromNum(), 
						item.getNumOfDay(), item.getSustainTime(), item.isHaveCourse() ? 1 : 0, TimeUtil.getCurrentTime(), DEFAULT_TYPE });
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
//			db.close();
		}
	}

	/////////////// UPDATE ///////////////////////////////////
	// 通过ID更新数据
	public void update(BookCollectItem item) {
	}

	////////////// QUERY  ///////////////////////////////////
	public ArrayList<EverydayCourse> query() {
		ArrayList<EverydayCourse> items = new ArrayList<EverydayCourse>();
		try {
			Cursor c = db.rawQuery("SELECT * FROM " + TABLE_COURSE_CHART, null);
			for (int i = 0; i < 7; i++) {
				EverydayCourse everydayCourse = new EverydayCourse();
				
				for (int j = 0; j < 5; j++) {
					boolean ok = c.moveToNext();
					if (ok) {
						SingleCourseInfo item = new SingleCourseInfo();
						
						item.setId(String.valueOf(c.getInt(c.getColumnIndex("_id"))));
						item.setCourseName(c.getString(c.getColumnIndex("course_name")));
						item.setNumOfWeek(c.getInt(c.getColumnIndex("day_of_week")));
						item.setTeacherName(c.getString(c.getColumnIndex("teacher")));
						item.setClassromNum(c.getString(c.getColumnIndex("place")));
						item.setNumOfDay(c.getString(c.getColumnIndex("course_time")));
						item.setSustainTime(c.getString(c.getColumnIndex("duration_week")));
						if (c.getInt(c.getColumnIndex("have_course")) == 1) {
							item.setHaveCourse(true);
						} else {
							item.setHaveCourse(false);
						}
						everydayCourse.getDayOfWeek().add(item);
					}
				}
				items.add(everydayCourse);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			db.close();
		}
		
		return items;
	}
	
	/////////////// DELETE ///////////////////////////////////
	// 根据数据库ID删除记录
	public void deleteByID(String ID) {
		db.delete(TABLE_COURSE_CHART, "_id=?", new String[] { ID });
	}

	// 删除数据表中所有记录
	public void deleteAllData() {
		try {
			db.execSQL("DELETE FROM " + TABLE_COURSE_CHART);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			db.close();
		}
	}

	// Close DB
	public void closeDB() {
		db.close();
	}
}
