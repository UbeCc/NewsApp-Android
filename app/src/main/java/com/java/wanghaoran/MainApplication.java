package com.java.wanghaoran;

import android.app.Application;
import android.content.Context;
import android.view.View;

import androidx.fragment.app.FragmentContainerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.java.wanghaoran.containers.User;
import com.java.wanghaoran.service.DBManager;
import com.java.wanghaoran.service.MySQLiteOpenHelper;
import com.java.wanghaoran.service.NewsManager;

import lombok.Getter;
import lombok.Setter;

public class MainApplication extends Application {
    private static Context context;
    private static BottomNavigationView bottomNavigationView;
    @Setter
    @Getter
    private static FragmentContainerView topFragmentContainer;
    public static View NewsList = null;
    public static User myUser ;
    public static boolean newsPage = true;
    public static boolean searchPage = false;
    public static boolean userPage = false;
    public static boolean newsPageisSearchingPage = false;
    //public static NewsManager newsManager;
    public static DBManager dbManager;



    @Override
    public void onCreate() {
        super.onCreate();
        MySQLiteOpenHelper mySQLiteOpenHelper;
        mySQLiteOpenHelper = new MySQLiteOpenHelper(this);
        context = getApplicationContext();
        //context = MainActivity.this;

        myUser = new User();
        new MySQLiteOpenHelper(context);
        dbManager = new DBManager();
        // newsManager =  NewsManager.getInstance();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        DBManager.closeDB();
    }

    public static void setNavView(BottomNavigationView b){
        bottomNavigationView = b;
    }
    public static BottomNavigationView getNavView(){
        return bottomNavigationView;
    }

    public static Context getContext(){
        return context;
    }
}
