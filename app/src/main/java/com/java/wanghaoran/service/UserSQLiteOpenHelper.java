package com.java.wanghaoran.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserSQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "userdb";
    private static final int DATABASE_VERSION = 1;

    public UserSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUserTable = "CREATE TABLE IF NOT EXISTS " + DATABASE_NAME + " (id INTEGER PRIMARY KEY, username TEXT UNIQUE, password TEXT)";
        db.execSQL(createUserTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);

        db.insert(DATABASE_NAME, null, values);

        db.close();
    }

    public boolean checkUserExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(DATABASE_NAME, null, "username=?", new String[]{username}, null, null, null);

        boolean exists = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return exists;
    }

    public int checkLoginCredentials(String username, String password) {
        Log.d("Login", "`checker` executed");
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor1 = db.query(DATABASE_NAME, null, "username=?", new String[]{username}, null, null, null);
        Cursor cursor2 = db.query(DATABASE_NAME, null, "username=? AND password=?", new String[]{username, password}, null, null, null);

        boolean hasUsername = cursor1.getCount() > 0;
        boolean passwdMatch = cursor2.getCount() > 0;

        cursor1.close();
        cursor2.close();
        db.close();

        if(!hasUsername) { // 用户名不存在，需要注册
            return 1;
        }
        else if(!passwdMatch) { // 用户名密码不匹配
            return 2;
        }
        else { // 登陆成功
            return 0;
        }
    }

    public void updatePassword(String username, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("password", newPassword);

        db.update(DATABASE_NAME, values, "username=?", new String[]{username});

        db.close();
    }
}