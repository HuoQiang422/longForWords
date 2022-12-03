package com.hanli.longforwords01.mydb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyUserList extends SQLiteOpenHelper {
    public MyUserList( Context context) {
        super(context, "userList.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table userList(_id INTEGER PRIMARY KEY AUTOINCREMENT,name varchar(20),account varchar(11),password varchar(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
