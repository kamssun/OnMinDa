package com.newthread.android.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by lanqx on 2014/4/17.
 */
public class DatabaseManager<T> {

    private T ob; //定义泛型成员变量
    private String tableName = "";
    private String DB_NAME = "";

    private List<String> fieldsStr = new ArrayList<String>();
    private List<Field> fields = new ArrayList<Field>();

    private List<Method> getMethods = new ArrayList<Method>();
    private List<Method> setMethods = new ArrayList<Method>();

    private Context context;
    private DatabaseHelper db;
    private Class<T> kind;

    public DatabaseManager(Context context, Class<T> kind) {
        this.context = context;
        this.kind = kind;
    }

    public void openTable(String tableName, String DB_NAME) {
        this.tableName = tableName;
        this.DB_NAME = DB_NAME;
        this.fieldsStr.clear();
        this.fields.clear();
        this.getMethods.clear();
        this.setMethods.clear();
        getMethods(kind);

        String CREATE_TBL = "CREATE TABLE IF NOT EXISTS " + tableName + "(_id INTEGER PRIMARY KEY AUTOINCREMENT,";
        int size = fieldsStr.size();
        for (int i = 0; i < size; i++) {
            String str = fieldsStr.get(i);
            if (i == size - 1) {
                CREATE_TBL = CREATE_TBL + str + " " + getSQLitType(fields.get(i)) + ");";
            } else {
                CREATE_TBL = CREATE_TBL + str + " " + getSQLitType(fields.get(i)) + ",";
            }
        }
        db = new DatabaseHelper(context, CREATE_TBL, DB_NAME, 1);
        db.execSQL(CREATE_TBL);

        db.close();
    }

    public void add(T obj) {
        db.getWritableDatabase();

        int size = fieldsStr.size();
        ContentValues values = new ContentValues();
        for (int i = 0; i < size; i++) {
            try {
                values.put(fieldsStr.get(i).toString(), String.valueOf(getMethods.get(i).invoke(obj, null)));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        db.insert(values);
        db.close();
    }

    public void add(List<T> objs) {
        db.getWritableDatabase();
        for (T obj : objs) {
            int size = fieldsStr.size();
            ContentValues values = new ContentValues();
            for (int i = 0; i < size; i++) {
                try {
                    values.put(fieldsStr.get(i).toString(), String.valueOf(getMethods.get(i).invoke(obj, null).toString()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            db.insert(values);
        }
        db.close();
    }

    public void delete(int id) {
        db.delete(id);
        db.close();
    }

    public void delete(T obj) {

        String whereClause = "";
        List<String> list = new ArrayList<String>();
        int size = fieldsStr.size();
        for (int i = 0; i < size; i++) {
            Method method = getMethods.get(i);
            try {
                String str = (String) getMethods.get(i).invoke(obj, null) + "";
                if (str != null && !str.equals("")) {
                    whereClause = whereClause + fieldsStr.get(i) + "=? and ";
                    list.add(str);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        size = list.size();
        String[] whereArgs = new String[size];
        for (int i = 0; i < size; i++) {
            whereArgs[i] = list.get(i);
        }
        whereClause = whereClause.substring(0, whereClause.lastIndexOf(" and "));
        db.delete(whereClause, whereArgs);
        db.close();
    }

    public void deleteTable(String tableName) {
        db.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
    }

    public void deleteTable() {
        db.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
    }
    public void cleanTable() {
        db.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
        openTable(tableName, DB_NAME);
    }
    public T query(int id) {
        try {

            T t = kind.newInstance();
            db.getReadableDatabase();
            Cursor c = db.rawQuery("select * from " + tableName + " where _id=?", new String[]{String.valueOf(id)});
            while (c.moveToNext()) {
                int size = fieldsStr.size();
                for (int i = 0; i < size; i++) {
                    setMethods.get(i).invoke(t, dataTransfer(fields.get(i), fieldsStr.get(i), c));
                }
            }
            return t;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<T> query() {
        try {
            List<T> list = new ArrayList<T>();

            db.getReadableDatabase();
            Cursor c = db.query();
            while (c.moveToNext()) {
                T t = kind.newInstance();
                int size = fieldsStr.size();
                for (int i = 0; i < size; i++) {
                    setMethods.get(i).invoke(t, dataTransfer(fields.get(i), fieldsStr.get(i), c));
                }
                list.add(t);
            }
            return list;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<T> query(T obj) {
        try {
            //获取whereClause和whereArgs
            String whereClause = "";
            List<String> args = new ArrayList<String>();
            int size = fieldsStr.size();
            for (int i = 0; i < size; i++) {
                String str = getMethods.get(i).invoke(obj, null) + "";
                if (str != null && !str.equals("")) {
                    whereClause = whereClause + fieldsStr.get(i) + "=? and ";
                    args.add(str);
                }
            }
            size = args.size();
            String[] whereArgs = new String[size];
            for (int i = 0; i < size; i++) {
                whereArgs[i] = args.get(i);
            }
            whereClause = whereClause.substring(0, whereClause.lastIndexOf(" and "));
            Loger.V(whereClause);

            List<T> list = new ArrayList<T>();
            T t = kind.newInstance();
            db.getReadableDatabase();
            Cursor c = db.rawQuery("select * from " + tableName + " where " + whereClause, whereArgs);
            while (c.moveToNext()) {
                size = fieldsStr.size();
                for (int i = 0; i < size; i++) {
                    setMethods.get(i).invoke(t, dataTransfer(fields.get(i), fieldsStr.get(i), c));
                }
                list.add(t);
            }
            return list;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getSQLitSize() {
        db.getReadableDatabase();
        Cursor c = db.query();
        return c.getCount();
    }

    private void getMethods(Class<T> kind) {
        Field[] fields = kind.getDeclaredFields();
        Method[] methods = kind.getMethods();

        List<String> fieldsStrT = new ArrayList<String>();
        List<Field> fieldsT = new ArrayList<Field>();
        for (Field field : fields) {
            if (classFilter(field)) {
                String fieldStr = field.toString();
                String str = fieldStr.substring(fieldStr.lastIndexOf(".") + 1, fieldStr.length());
                fieldsStrT.add(str);
                fieldsT.add(field);
            }
        }

//        List<Method> temps = new ArrayList<Method>();
        for (Method method : methods) {
            String methodStr = method.toString();
            for (String fStr : fieldsStrT) {
                if (isUpperCase(fStr.charAt(1))) {
                    if (methodStr.contains("get" + fStr + "()")) {
                        this.getMethods.add(method);
                        this.fieldsStr.add(fStr);
                        this.fields.add(fieldsT.get(fieldsStrT.indexOf(fStr)));
                    }
                    if (methodStr.contains("set" + fStr)) {
                        this.setMethods.add(method);
                    }
                } else {
                    if (methodStr.contains("get" + fStr.substring(0, 1).toUpperCase() + fStr.substring(1) + "()")) {
                        this.getMethods.add(method);
                        this.fieldsStr.add(fStr);
                        this.fields.add(fieldsT.get(fieldsStrT.indexOf(fStr)));
                    }
                    if (methodStr.contains("set" + fStr.substring(0, 1).toUpperCase() + fStr.substring(1))) {
                        this.setMethods.add(method);
                    }
                }
            }

        }
    }

    public int isExist(String table) {
        int size = -1;
        String CREATE_TBL = "CREATE TABLE IF NOT EXISTS " + "sqlite_sequence" + "(name TEXT,seq INTEGER)";
        DatabaseHelper db = new DatabaseHelper(context, CREATE_TBL, DB_NAME, 1);
        db.getReadableDatabase();
        Cursor c = db.rawQuery("select * from " + "sqlite_sequence" + " where name=?", new String[]{table});
        while (c.moveToNext()) {
            size = c.getInt(c.getColumnIndex("seq"));
        }
        return size;
    }

    public String[] getAllTable() {
        String CREATE_TBL = "CREATE TABLE IF NOT EXISTS " + "sqlite_sequence" + "(name TEXT,seq INTEGER)";
        DatabaseHelper db = new DatabaseHelper(context, CREATE_TBL, DB_NAME, 1);
        db.getReadableDatabase();
        Cursor c = db.query();
        String[] tables = new String[c.getCount()];
        while (c.moveToNext()) {
            tables[c.getPosition()] = c.getString(c.getColumnIndex("name"));
        }
        return tables;
    }

    private boolean classFilter(Field field) {

        if (field.getType().equals(String.class)
                || field.getType().equals(int.class)
                || field.getType().equals(short.class)
                || field.getType().equals(long.class)
                || field.getType().equals(float.class)
                || field.getType().equals(double.class)
                || field.getType().equals(char.class)
                || field.getType().equals(byte.class)
                || field.getType().equals(boolean.class)) {
            return true;
        }
        return false;
    }

    private String getSQLitType(Field field) {
        if (field.getType().equals(String.class)
                || field.getType().equals(char.class)) {
            return "TEXT";//文本
        } else if (field.getType().equals(int.class)
                || field.getType().equals(short.class)
                || field.getType().equals(long.class)) {
            return "INTEGER";//整形
        } else if (field.getType().equals(double.class)) {
            return "REAL";//浮点型
        } else if (field.getType().equals(float.class)) {
            return "FLOAT";//浮点型
        } else if (field.getType().equals(byte.class)) {
            return "BLOB";//二进制
        } else if (field.getType().equals(boolean.class)) {
            return "BOOLEAN";//布尔
        } else {
            return "TEXT";//文本
        }
    }

    private <T> T dataTransfer(Field field, String fieldsStr, Cursor c) {
        if (field.getType().equals(String.class)) {
            return (T) c.getString(c.getColumnIndex(fieldsStr));//文本
        } else if (field.getType().equals(char.class)) {
            return (T) (Character) c.getString(c.getColumnIndex(fieldsStr)).charAt(0);//字符
        } else if (field.getType().equals(int.class)) {
            return (T) (Integer) c.getInt(c.getColumnIndex(fieldsStr));//整形
        } else if (field.getType().equals(short.class)) {
            return (T) (Short) c.getShort(c.getColumnIndex(fieldsStr));//短整型
        } else if (field.getType().equals(long.class)) {
            return (T) (Long) c.getLong(c.getColumnIndex(fieldsStr));//长整形
        } else if (field.getType().equals(float.class)) {
            return (T) (Float) c.getFloat(c.getColumnIndex(fieldsStr));//单精度浮点数
        } else if (field.getType().equals(double.class)) {
            return (T) (Double) c.getDouble(c.getColumnIndex(fieldsStr));//双精度浮点数
        } else if (field.getType().equals(byte.class)) {
            return (T) c.getBlob(c.getColumnIndex(fieldsStr));//二进制
        } else if (field.getType().equals(boolean.class)) {
            return (T) (Boolean) Boolean.getBoolean(c.getString(c.getColumnIndex(fieldsStr)));//布尔
        } else {
            return (T) c.getString(c.getColumnIndex(fieldsStr));//文本
        }
    }

    private boolean isUpperCase(char c) {
        if (c >= 'A' && c <= 'Z') {
            return true;
        } else {
            return false;
        }
    }
}
