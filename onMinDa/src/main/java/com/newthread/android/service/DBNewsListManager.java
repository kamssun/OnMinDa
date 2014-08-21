package com.newthread.android.service;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.newthread.android.bean.NewsListItem;
import com.newthread.android.util.DBHelper;
import com.newthread.android.util.StringUtils;
import com.newthread.android.util.TimeUtil;

public class DBNewsListManager {
	private DBHelper helper;
	private SQLiteDatabase db;
	private static final String TABLE_NEWS_LIST = "news_list";
	private static final String DEFAULT_TYPE = "0";
	
	public DBNewsListManager(Context context) {
		helper = new DBHelper(context);
		db = helper.getWritableDatabase();
	}
	
	/////////////// ADD ///////////////////////////////////
	// 添加单条数据
	public void add(NewsListItem item) {
		try {
			db.execSQL("INSERT INTO " + TABLE_NEWS_LIST + " VALUES(null, ?, ?, ?, ?, ?, ?)",
					new Object[] { item.getTitle(), item.getDigest(), item.getTime(), 
									item.getHref(), TimeUtil.getCurrentTime(), item.getType()});
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}	

	// 添加多条数据
	public void add(List<NewsListItem> items) {
		db.beginTransaction();
		try {
			for (NewsListItem item : items) {
				db.execSQL("INSERT INTO " + TABLE_NEWS_LIST + " VALUES(null, ?, ?, ?, ?, ?, ?)",
					new Object[] {  item.getTitle(), item.getDigest(), item.getTime(), 
									item.getHref(), TimeUtil.getCurrentTime(), item.getType()});
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
		} finally {
			db.endTransaction();
			db.close();
		}
	}

	/////////////// UPDATE ///////////////////////////////////
	// 更新数据
	public void updatetodo(NewsListItem item) {
	}

	////////////// QUERY  ///////////////////////////////////
	// 获取所有记录信息
	public ArrayList<NewsListItem> query() {
		String sql = "SELECT * FROM " + TABLE_NEWS_LIST + " ORDER BY add_time DESC";
		ArrayList<NewsListItem> items = new ArrayList<NewsListItem>();
		try {
			performGetData(sql, items);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		
		return items;
	}
	
	// 根据新闻类型获取记录
	public ArrayList<NewsListItem> queryByType(String type) {
		String sql = "SELECT * FROM " + TABLE_NEWS_LIST + " WHERE type = " + "'"+type+"'" +" ORDER BY add_time DESC";
		ArrayList<NewsListItem> items = new ArrayList<NewsListItem>();
		if (StringUtils.isEmpty(type)) {
			type = DEFAULT_TYPE;
		}
		performGetData(sql, items);
		
		return items;
	}
	
	// 数据抓取
	private void performGetData(String sql, ArrayList<NewsListItem> items) {
		Cursor c = db.rawQuery(sql, null);
		while (c.moveToNext()) {
			NewsListItem item = new NewsListItem();
			
			item.setId(String.valueOf(c.getInt(c.getColumnIndex("_id"))));
			item.setTitle((c.getString(c.getColumnIndex("title"))));
			item.setDigest((c.getString(c.getColumnIndex("digest"))));
			item.setTime(c.getString(c.getColumnIndex("time")));
			item.setHref(c.getString(c.getColumnIndex("href")));
			item.setType(c.getString(c.getColumnIndex("type")));
			
			items.add(item);
		}
	}

	/////////////// DELETE ///////////////////////////////////
	// 根据ID删除记录
	public void deleteByID(String id) {
		db.delete(TABLE_NEWS_LIST, "_id=?", new String[] {id});
	}
	
	// 更加类型删除记录
	public void deleteByType(String type) {
		db.delete(TABLE_NEWS_LIST, "type=?", new String[] {type});
	}

	// 删除数据表中所有记录
	public void deleteAllData() {
		try {
			db.execSQL("DELETE FROM " + TABLE_NEWS_LIST);
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
