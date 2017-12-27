package com.zhouwei.helloapt.demo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

/**
 * Created by zhouwei on 2017/12/23.
 */

public class StudentDBDemo extends SQLiteOpenHelper {


    public StudentDBDemo(Context context, String name) {
        super(context, Environment.getExternalStorageDirectory() + "/" + name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
