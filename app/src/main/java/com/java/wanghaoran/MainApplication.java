package com.java.wanghaoran;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.content.SharedPreferences;
import android.view.View;

import androidx.fragment.app.FragmentContainerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.java.wanghaoran.containers.User;
import com.java.wanghaoran.service.DBManager;
import com.java.wanghaoran.service.MySQLiteOpenHelper;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

public class MainApplication extends Application {
    public static MainApplication instance = new MainApplication();
    public static MainApplication getInstance() {
        return instance;
    }
    public static Map<String, String> userInfo = new HashMap<>();
    public static String username = "";
    public static String password = "";
    private static Context context;
    private static BottomNavigationView bottomNavigationView;
    @Setter
    @Getter
    private static FragmentContainerView topFragmentContainer;
    public static View NewsList = null;
    public static User myUser;
    public static boolean newsPage = true;
    public static boolean searchPage = false;
    public static boolean userPage = false;
    public static boolean newsPageisSearchingPage = false;
    public static DBManager dbManager;
    MySQLiteOpenHelper mySQLiteOpenHelper;

    public static void createDB() {
        Log.d("QWQ", "createDB: " + username);
        dbManager = new DBManager(username);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mySQLiteOpenHelper = new MySQLiteOpenHelper(this, username);
        context = getApplicationContext();
        if(User.getUser(username) == null) {
            myUser = User.addUser(username, password);
        }
        new MySQLiteOpenHelper(context, username);
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

    private SharedPreferences getSharedPreferencesForUser(String username) {
        return context.getSharedPreferences(username, Context.MODE_PRIVATE);
    }

    public void saveUserInfo(String username, String key, String value) {
        Log.d("User", username);
        SharedPreferences sharedPreferences = getSharedPreferencesForUser(username);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getUserInfo(String username, String key) {
        SharedPreferences sharedPreferences = getSharedPreferencesForUser(username);
        return sharedPreferences.getString(key, "");
    }

}