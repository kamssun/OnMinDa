package com.newthread.android.service;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.newthread.android.bean.CurrentBorrowItem;
import com.newthread.android.bean.LibraryCurrentBorrow;
import com.newthread.android.util.DBHelper;
import com.newthread.android.util.TimeUtil;

public class DBBookCurrentBorrowManager {
	private DBHelper helper;
	private SQLiteDatabase db;
	
	public static final String TABLE_CURRENT_BORROW = "current_borrow";
	public static final String DEFAULT_TYPE = "1";
	
	public DBBookCurrentBorrowManager(Context context) {
		helper = new DBHelper(context);
		db = helper.getWritableDatabase();
	}
	
	/////////////// ADD ///////////////////////////////////
	// 添加单条数据
	public void add(CurrentBorrowItem item) {
		try {
			db.execSQL("INSERT INTO " + TABLE_CURRENT_BORROW + " VALUES(null, ?, ?, ?, ?, ?)",
					new Object[] { item.getBookName(), item.getOverTime(), TimeUtil.getCurrentTime(), item.isCanRenew() ? 1 : 0, DEFAULT_TYPE });
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}

	// 添加多条数据
	public void add(List<CurrentBorrowItem> items) {
		db.beginTransaction();
		try {
			for (CurrentBorrowItem item : items) {
				db.execSQL("INSERT INTO " + TABLE_CURRENT_BORROW + " VALUES(null, ?, ?, ?, ?, ?)",
						new Object[] { item.getBookName(), item.getOverTime(), TimeUtil.getCurrentTime(), item.isCanRenew() ? 1 : 0, DEFAULT_TYPE });
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
	public void update(CurrentBorrowItem item) {
	}

	////////////// QUERY  ///////////////////////////////////
	public ArrayList<CurrentBorrowItem> query() {
		ArrayList<CurrentBorrowItem> items = new ArrayList<CurrentBorrowItem>();
		try {
			Cursor c = db.rawQuery("SELECT * FROM " + TABLE_CURRENT_BORROW + " ORDER BY add_time DESC", null);
			while (c.moveToNext()) {
				CurrentBorrowItem item = new CurrentBorrowItem();
				
				item.setId(String.valueOf(c.getInt(c.getColumnIndex("_id"))));
				item.setBookName(c.getString(c.getColumnIndex("book_name")));
				item.setOverTime(c.getString(c.getColumnIndex("over_time")));
				item.setCanRenew(c.getInt(c.getColumnIndex("can_renew")) == 1 ? true : false);
				
				items.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return items;
	}
	
	// 根据索书号：id，判断图书是否存在
	public boolean haveCollected(String book_id) {
		Cursor c = db.rawQuery("SELECT book_id FROM " + TABLE_CURRENT_BORROW + " where _id = " + "'" + book_id.trim() + "'", null);
		
		if (c != null && c.getCount() > 0) {
			return true;
		}
		
		return false;
	}

	/////////////// DELETE ///////////////////////////////////
	// 根据数据库ID删除记录
	public void deleteByID(String ID) {
		db.delete(TABLE_CURRENT_BORROW, "_id=?", new String[] { ID });
	}

	// 删除数据表中所有记录
	public void deleteAllData() {
		try {
			db.execSQL("DELETE FROM " + TABLE_CURRENT_BORROW);
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
