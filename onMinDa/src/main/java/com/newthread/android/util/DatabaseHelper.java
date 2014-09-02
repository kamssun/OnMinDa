package com.newthread.android.util;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by lanqx on 2014/4/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	private String CREATE_TBL = "";
	private String TBL_NAME = "";

	private Context context;
	private SQLiteDatabase db;

	public DatabaseHelper(Context context, String CREATE_TBL, String DB_NAME,
			int version) {
		super(context, DB_NAME, null, version);
		this.CREATE_TBL = CREATE_TBL;
		this.TBL_NAME = getTBL_NAME(CREATE_TBL);
	}

	public DatabaseHelper(Context context, String name,
			SQLiteDatabase.CursorFactory cursorFactory, int version) {
		super(context, name, cursorFactory, version);
	}

	public void insert(ContentValues values) {
		SQLiteDatabase db = getWritableDatabase();
		db.insert(TBL_NAME, null, values);
		db.close();
	}

	public void delete(int id) {
		db = getWritableDatabase();
		db.delete(TBL_NAME, "_id=?", new String[] { String.valueOf(id) });
	}

	public void delete(String whereClause, String[] whereArgs) {
		db = getWritableDatabase();
		db.delete(TBL_NAME, whereClause, whereArgs);
	}

	public Cursor query() {
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(TBL_NAME, null, null, null, null, null, null);
		return c;
	}

	public Cursor rawQuery(String sql, String[] selectionArgs) {
		db = getReadableDatabase();
		return db.rawQuery(sql, selectionArgs);
	}

	public void execSQL(String sql) {
		db = getWritableDatabase();
		db.execSQL(sql);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO 创建数据库后，对数据库的操作
		this.db = db;
		db.execSQL(CREATE_TBL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO 更改数据库版本的操作
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		// TODO 每次成功打开数据库后首先被执行
	}

	private String getTBL_NAME(String CREATE_TBL) {
		String str = CREATE_TBL;
		return str.substring(26, str.indexOf("("));
	}
}