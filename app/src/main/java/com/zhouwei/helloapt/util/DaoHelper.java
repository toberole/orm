package com.zhouwei.helloapt.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zhouwei.helloapt.dao.DaoMaster;
import com.zhouwei.helloapt.db.Saveable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouwei on 2017/12/20.
 */

public class DaoHelper {
    private Context context;
    private volatile boolean isInit = false;

    public void save(Saveable saveable) {
        if (assertinit()) {
            String tableName = saveable.getClass().getSimpleName();
            SQLiteDatabase db = DaoMaster.getDaoSession(context).getWritableDatabase();
            ContentValues values = new ContentValues();
            saveable.save(values);
            if (values.keySet().size() > 0) {
                db.insert(tableName, null, values);
            }
            db.close();
        }
    }

    public <T> List<T> get(Class clazz, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        List<T> res = null;
        if (assertinit()) {
            SQLiteOpenHelper dbSQLiteOpenHelper = DaoMaster.getDaoSession(context);
            SQLiteDatabase db = dbSQLiteOpenHelper.getReadableDatabase();
            Cursor cursor = db.query(clazz.getSimpleName(), null, selection, selectionArgs, groupBy, having, orderBy);
            res = generateResults(clazz, cursor);
            db.close();
        }
        return res;
    }

    public <T> List<T> get(Class clazz, int offset, int rowCount) {
        List<T> res = null;
        if (assertinit()) {
            SQLiteOpenHelper dbSQLiteOpenHelper = DaoMaster.getDaoSession(context);
            SQLiteDatabase db = dbSQLiteOpenHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(" SELECT * FROM " + clazz.getSimpleName() + " LIMIT " + rowCount + " OFFSET " + offset, null);
            res = generateResults(clazz, cursor);
            db.close();
        }
        return res;
    }

    public void delete(Class clazz, String whereClause, String[] whereArgs) {
        if (assertinit()) {
            SQLiteOpenHelper dbSQLiteOpenHelper = DaoMaster.getDaoSession(context);
            SQLiteDatabase db = dbSQLiteOpenHelper.getReadableDatabase();
            db.delete(clazz.getSimpleName(), whereClause, whereArgs);
        }
    }

    public void update(Class clazz, ContentValues values, String whereClause, String[] whereArgs) {
        if (assertinit()) {
            SQLiteOpenHelper dbSQLiteOpenHelper = DaoMaster.getDaoSession(context);
            SQLiteDatabase db = dbSQLiteOpenHelper.getReadableDatabase();
            db.update(clazz.getSimpleName(), values, whereClause, whereArgs);
        }
    }


    public List generateResults(Class clazz, Cursor cursor) {
        List res = new ArrayList();
        if (null != cursor) {
            while (cursor.moveToNext()) {
                try {
                    String[] columnNames = cursor.getColumnNames();
                    ContentValues values = new ContentValues();
                    for (int i = 0; i < columnNames.length; i++) {
                        String columnName = columnNames[i];
                        String columnValue = cursor.getString(cursor.getColumnIndex(columnName));
                        values.put(columnNames[i], columnValue);
                    }

                    Saveable saveable = (Saveable) clazz.newInstance();
                    saveable.newInstance(values);
                    res.add(saveable);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return res;
    }

    public synchronized void init(Context context) {
        if (!isInit) {
            this.context = context.getApplicationContext();
            isInit = true;
        }
    }

    public boolean assertinit() {
        return isInit;
    }

    private DaoHelper() {
    }

    private static class DBEngineHolder {
        public static DaoHelper instance = new DaoHelper();
    }

    public static DaoHelper getInstance() {
        return DBEngineHolder.instance;
    }
}
