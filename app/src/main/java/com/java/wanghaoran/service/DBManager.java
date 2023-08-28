// 同一条新闻，在不同用户的db里面，会有不同的id，所以不能用id来判断是否重复!!!

package com.java.wanghaoran.service;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.java.wanghaoran.MainApplication;
import com.java.wanghaoran.containers.News;

public final class DBManager {
    private static SQLiteDatabase db;
    private static MySQLiteOpenHelper helper;
    private static Map<Long, News> newsInStore = new HashMap<>();

    public DBManager(String dbName) {
        Log.d("Logger", "CreateTable: " + dbName);
        helper = new MySQLiteOpenHelper(MainApplication.getContext(), dbName);
//        Log.d("QWQ", "1helper.dbname: " + helper.getDatabaseName() + " QWQ " + helper.databaseName);
        db = helper.getWritableDatabase();
//        Log.d("QWQ", "3helper.dbname: " + helper.getDatabaseName() + " QWQ " + helper.databaseName);
        helper.onCreate(db);
//        Log.d("QWQ", "2helper.dbname: " + helper.getDatabaseName() + " QWQ " + helper.databaseName);
    }

    /**
     * 向数据库中添加新闻
     * @param news Map<Long, News>要添加的新闻和其对应编号
     */
    public static void add(Map<Long, News> news) {
        Long newsSize = Long.valueOf(news.size());
        Long newsInStoreSize = Long.valueOf(newsInStore.size());
        int counter  = 0;
        db.beginTransaction();

        try {
            for (Long i = newsInStoreSize; i < newsSize; i++) {
                News mNews = news.get(i);
                newsInStore.put(i, mNews);
                counter++;
                // 这里已经转换成Gson格式了
                db.execSQL("INSERT OR IGNORE INTO " + helper.getDatabaseName() + " VALUES(?,?)", new Object[]{(int)((long)i), mNews.toString()});
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * 查询现在数据库中的所有新闻
     * @return ret List<News>所有新闻
     */
    public static List<News> query(){
        Cursor cursor = db.rawQuery("SELECT * FROM " + helper.getDatabaseName(), null);
        ArrayList<News> ret = new ArrayList<>();
        Gson gson = new Gson();
        while(cursor.moveToNext()){
            News item =  gson.fromJson(cursor.getString(1), News.class) ;
            ret.add(item);
        }
        return ret;
    }

    /**
     * 关闭数据库
     */
    public static void closeDB(){
        db.close();
    }
}