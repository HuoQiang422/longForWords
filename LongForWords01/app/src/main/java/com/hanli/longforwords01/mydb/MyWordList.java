package com.hanli.longforwords01.mydb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyWordList extends SQLiteOpenHelper {

    public MyWordList(@Nullable Context context,String name) {
        super(context, name+".db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table wordList(_id INTEGER PRIMARY KEY AUTOINCREMENT,word varchar(20),exp varchar(50))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
