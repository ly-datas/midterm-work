package com.example.notebook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyHelper extends SQLiteOpenHelper {

    public MyHelper(Context context) {
        super(context,"notesdb.db",null,1);
    }

    //  数据库第一次被创建时调用该方法
    @Override
    public void onCreate(SQLiteDatabase db) {
//        初始化数据库的表结构，执行一条建表的SQL语句
//        db.execSQL("CREATE TABLE notes(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
//                "title VARCHAR(40), content VARCHAR(40),crDate VARCHAR(20),mdDate VARCHAR(20))");
        db.execSQL("create table notes(_id integer primary key autoincrement, title char(10), content char(20), crDate char(20),mdDate char(20))");
    }

    //    当数据库的版本号增加时调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
