package com.newthread.android.service;

import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.newthread.android.bean.PersonalInfo;
import com.newthread.android.util.DBHelper;
import com.newthread.android.util.TimeUtil;

public class DBPersonalInfoManager {
	private DBHelper helper;
	private SQLiteDatabase db;
	
	public static final String TABLE_PERSONAL_INFO = "personal_info";
	public static final String DEFAULT_TYPE = "1";
	
	public DBPersonalInfoManager(Context context) {
		helper = new DBHelper(context);
		db = helper.getWritableDatabase();
	}
	
	/////////////// ADD ///////////////////////////////////
	// 添加单条数据
	public void add(PersonalInfo item) {
		try {
			db.execSQL("INSERT INTO " + TABLE_PERSONAL_INFO + " VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?)",
					new Object[] { item.getName(), item.getGender(), item.getCollege(), item.getBorrowNum(), 
								item.getSid(), item.getBar(), TimeUtil.getCurrentTime(), DEFAULT_TYPE });
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}

	// 添加多条数据
	public void add(List<PersonalInfo> items) {
		db.beginTransaction();
		try {
			for (PersonalInfo item : items) {
				db.execSQL("INSERT INTO " + TABLE_PERSONAL_INFO + " VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?)",
						new Object[] {item.getName(), item.getGender(), item.getCollege(), item.getBorrowNum(), 
						item.getSid(), item.getBar(), TimeUtil.getCurrentTime(), DEFAULT_TYPE });
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
			db.close();
		}
	}

	/////////////// UPDATE ///////////////////////////////////
	// 通过ID更新数据
	public void update(PersonalInfo item) {
	}

	////////////// QUERY  ///////////////////////////////////
	public PersonalInfo query() {
		PersonalInfo item = new PersonalInfo();
		try {
			Cursor c = db.rawQuery("SELECT * FROM " + TABLE_PERSONAL_INFO + " ORDER BY add_time DESC", null);
			while (c.moveToNext()) {
				item.setId(String.valueOf(c.getInt(c.getColumnIndex("_id"))));
				item.setName(c.getString(c.getColumnIndex("name")));
				item.setBar(c.getString(c.getColumnIndex("bar_code_id")));
				item.setSid(c.getString(c.getColumnIndex("student_id")));
				item.setBorrowNum(c.getString(c.getColumnIndex("borrow_num")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return item;
	}
	
	public boolean haveCollected(String book_id) {
		Cursor c = db.rawQuery("SELECT book_id FROM " + TABLE_PERSONAL_INFO + " where _id = " + "'" + book_id.trim() + "'", null);
		
		if (c != null && c.getCount() > 0) {
			return true;
		}
		
		return false;
	}

	/////////////// DELETE ///////////////////////////////////
	// 根据数据库ID删除记录
	public void deleteByID(String ID) {
		db.delete(TABLE_PERSONAL_INFO, "_id=?", new String[] { ID });
	}

	// 删除数据表中所有记录
	public void deleteAllData() {
		try {
			db.execSQL("DELETE FROM " + TABLE_PERSONAL_INFO);
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
