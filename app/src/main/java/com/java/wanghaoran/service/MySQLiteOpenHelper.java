package com.java.wanghaoran.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    private final static String DEFAULTNAME = "defaultUser";
    public String databaseName;

    public String getDatabaseName() {
        return databaseName;
    }

    public MySQLiteOpenHelper(@Nullable Context context, String dbName) {
        super(context, (dbName == "" ? DEFAULTNAME : dbName) + ".db", null, 1);
        databaseName = (dbName == "") ? DEFAULTNAME : dbName;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d("Logger", "QWQ" + "createAgain" + databaseName);
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + databaseName + " (_id INTEGER PRIMARY KEY UNIQUE, info TEXT)");}

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("ALTER TABLE " + databaseName + " ADD COLUMN other STRING");
    }

}