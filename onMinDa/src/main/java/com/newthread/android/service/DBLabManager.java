package com.newthread.android.service;

import java.util.ArrayList;
import java.util.List;

import com.newthread.android.ui.labquery.LabDetail;
import com.newthread.android.util.DBHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBLabManager {
	private DBHelper helper;
	private SQLiteDatabase db;

	public static final String DEFAULT_TYPE = "1";
	public static final String TABLE_LAB_CHART = "Lab";

	public DBLabManager(Context context) {
		helper = new DBHelper(context);
		db = helper.getWritableDatabase();
	}

	// ///////////// ADD ///////////////////////////////////
	// 添加单条数据
	public void add(LabDetail item) {
		try {
			db.execSQL(
					"INSERT INTO " + TABLE_LAB_CHART
							+ " VALUES(null,?,?,?,?,?,?,?)",
					new Object[] { item.getLabName(), item.getWeekNum(),
							item.getWeek(), item.getTime(),
							item.getLabLocation(), item.getTeacherName(),
							item.getScore() });
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}

	// 添加多条数据
	public void add(List<LabDetail> items) {
		db.beginTransaction();
		try {
			for (LabDetail item : items) {
				db.execSQL(
						"INSERT INTO " + TABLE_LAB_CHART
								+ " VALUES(null,?,?,?,?,?,?,?)",
						new Object[] { item.getLabName(), item.getWeekNum(),
								item.getWeek(), item.getTime(),
								item.getLabLocation(), item.getTeacherName(),
								item.getScore() });
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
			db.close();
		}
	}

	// ///////////// UPDATE ///////////////////////////////////
	// 通过ID更新数据
	public void update(LabDetail item) {
		// ContentValues cv = new ContentValues();
		//
		// cv.put("description", item.getDescription());
		// cv.put("receive_time", item.getReceive_time());
		// cv.put("notification_type", item.getNotification_type());
		// db.update(TABLE_BOOK_COLLECT, cv, "_id=?", new String[] {
		// item.getID() });
	}

	// //////////// QUERY ///////////////////////////////////
	public ArrayList<LabDetail> query() {
		ArrayList<LabDetail> items = new ArrayList<LabDetail>();
		try {
			Cursor c = db.rawQuery("SELECT * FROM " + TABLE_LAB_CHART
					+ " ORDER BY WeekNum ASC", null);
			while (c.moveToNext()) {
				LabDetail item = new LabDetail();

				item.setLabName(c.getString(c.getColumnIndex("LabName")));
				item.setWeekNum(c.getString(c.getColumnIndex("WeekNum")));
				item.setWeek(c.getString(c.getColumnIndex("Week")));
				item.setTime(c.getString(c.getColumnIndex("Time")));
				item.setLabLocation(c.getString(c.getColumnIndex("LabLocation")));
				item.setTeacherName(c.getString(c.getColumnIndex("TeacherName")));
				item.setScore(c.getString(c.getColumnIndex("Score")));

				items.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}

		return items;
	}

	// 删除单条记录
	public void deleteByID(String ID) {
		db.delete(TABLE_LAB_CHART, "_id=?", new String[] { ID });
	}

	// 删除数据表中所有记录
	public void deleteAllData() {
		try {
			db.execSQL("DELETE FROM " + TABLE_LAB_CHART);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}

	// Close DB
	public void closeDB() {
		db.close();
	}
}
