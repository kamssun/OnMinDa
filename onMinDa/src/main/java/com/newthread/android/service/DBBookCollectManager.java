package com.newthread.android.service;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.newthread.android.bean.BookCollectItem;
import com.newthread.android.util.DBHelper;
import com.newthread.android.util.TimeUtil;

public class DBBookCollectManager {
	private DBHelper helper;
	private SQLiteDatabase db;
	
	public static final String TABLE_BOOK_COLLECT = "book_collect";
	public static final String DEFAULT_TYPE = "1";
	
	public DBBookCollectManager(Context context) {
		helper = new DBHelper(context);
		db = helper.getWritableDatabase();
	}
	
	/////////////// ADD ///////////////////////////////////
	// 添加单条数据
	public void add(BookCollectItem item) {
		try {
			db.execSQL("INSERT INTO " + TABLE_BOOK_COLLECT + " VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?)",
					new Object[] { item.getTitle(), item.getPublisher(), item.getAuther(), item.getId(), 
									item.getPlace(), item.getUrl(), TimeUtil.getCurrentTime(), DEFAULT_TYPE });
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}

	// 添加多条数据
	public void add(List<BookCollectItem> items) {
		db.beginTransaction();
		try {
			for (BookCollectItem item : items) {
				db.execSQL("INSERT INTO " + TABLE_BOOK_COLLECT + " VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?)",
						new Object[] {item.getTitle(), item.getPublisher(), item.getAuther(), item.getId(), 
									item.getPlace(), item.getUrl(), TimeUtil.getCurrentTime(), DEFAULT_TYPE });
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
	public void update(BookCollectItem item) {
//		ContentValues cv = new ContentValues();
//		
//		cv.put("description", item.getDescription());
//		cv.put("receive_time", item.getReceive_time());
//		cv.put("notification_type", item.getNotification_type());
//		db.update(TABLE_BOOK_COLLECT, cv, "_id=?", new String[] { item.getID() });
	}

	////////////// QUERY  ///////////////////////////////////
	public ArrayList<BookCollectItem> query() {
		ArrayList<BookCollectItem> items = new ArrayList<BookCollectItem>();
		try {
			Cursor c = db.rawQuery("SELECT * FROM " + TABLE_BOOK_COLLECT + " ORDER BY add_time DESC", null);
			while (c.moveToNext()) {
				BookCollectItem item = new BookCollectItem();
				
				item.setID(String.valueOf(c.getInt(c.getColumnIndex("_id"))));
				item.setAuther(c.getString(c.getColumnIndex("auther")));
				item.setPlace(c.getString(c.getColumnIndex("place")));
				item.setPublisher(c.getString(c.getColumnIndex("publisher")));
				item.setTitle(c.getString(c.getColumnIndex("book_name")));
				item.setAddTime(c.getString(c.getColumnIndex("add_time")));
				item.setType(c.getString(c.getColumnIndex("type")));
				item.setId(c.getString(c.getColumnIndex("book_id")));
				item.setUrl(c.getString(c.getColumnIndex("url")));
				
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
		Cursor c = db.rawQuery("SELECT book_id FROM " + TABLE_BOOK_COLLECT + " where book_id = " + "'" + book_id.trim() + "'", null);
		
		if (c != null && c.getCount() > 0) {
			return true;
		}
		
		return false;
	}

	/////////////// DELETE ///////////////////////////////////
	// 根据数据库ID删除记录
	public void deleteByID(String ID) {
		db.delete(TABLE_BOOK_COLLECT, "_id=?", new String[] { ID });
	}

	// 删除数据表中所有记录
	public void deleteAllData() {
		try {
			db.execSQL("DELETE FROM " + TABLE_BOOK_COLLECT);
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
