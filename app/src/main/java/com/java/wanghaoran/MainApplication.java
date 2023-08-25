package com.java.wanghaoran;

import android.app.Application;
import android.content.Context;
import android.view.View;

import androidx.fragment.app.FragmentContainerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.java.wanghaoran.Containers.User;
import com.java.wanghaoran.Utils;
import com.java.wanghaoran.Containers;

//        import com.java.lichenghao.newsrefactored.service.DBManager;
//        import com.java.lichenghao.newsrefactored.service.MySQLiteOpenHelper;
//        import com.java.lichenghao.newsrefactored.service.NewsManager;

public class MainApplication extends Application {
    private static Context context;
    private static BottomNavigationView bottomNavigationView;
    private static FragmentContainerView topFragmentContainer;
    public static View NewsList = null;
    public static User myUser;
    static{myUser = new Containers().new User();}
    public static boolean newsPage = true;
    public static boolean searchPage = false;
    public static boolean userPage = false;
    public static boolean newsPageisSearchingPage = false;
    //public static NewsManager newsManager;
//    public static DBManager dbManager;

    public static FragmentContainerView getTopFragmentContainer() {
        return topFragmentContainer;
    }

    public static void setTopFragmentContainer(FragmentContainerView topFragmentContainer) {
        MainApplication.topFragmentContainer = topFragmentContainer;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        MySQLiteOpenHelper mySQLiteOpenHelper;
//        mySQLiteOpenHelper = new MySQLiteOpenHelper(this);
        context = getApplicationContext();
        Containers myContainers = new Containers();
        User myUser = myContainers.new User();
//        new MySQLiteOpenHelper(context);
//        dbManager = new DBManager();
        // newsManager =  NewsManager.getInstance();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
//        DBManager.closeDB();
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