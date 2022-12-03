package com.hanli.longforwords01.mydb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ShareWordsList extends SQLiteOpenHelper {
    public ShareWordsList(@Nullable Context context) {
        super(context, "shareWords.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table share_words(_id INTEGER PRIMARY KEY AUTOINCREMENT,author varchar(20),word varchar(20),exp varchar(50))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
