package com.java.wanghaoran;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.java.wanghaoran.containers.Keywords;
import com.java.wanghaoran.databinding.ActivityMainBinding;
import com.java.wanghaoran.service.APIManager;
import com.java.wanghaoran.ui.NewsListFragment;
import com.java.wanghaoran.ui.SearchFragment;
import com.java.wanghaoran.ui.SelectPaddleFragment;
import com.java.wanghaoran.ui.TabListFragment;
import com.java.wanghaoran.ui.UserPageFragment;

public class AppActivity extends AppCompatActivity  implements TabListFragment.onTabBarListener,
        SelectPaddleFragment.onSelectPaddleListener, SearchFragment.OnSearchInputFinished {
    private long exitTime = 0;
    private ActivityMainBinding binding;
    public BottomNavigationView navView;
    long firstTime = 0;

    public SearchView searchView;
    public FragmentContainerView mainArea;
    public FragmentContainerView tabs;
    private SelectPaddleFragment selectPaddleFragment;
    private FragmentManager fragmentManager;
    private DrawerLayout drawerLayout;
    private TabListFragment tabListFragment;
    private NewsListFragment newsListFragment;

    @Override
    public void selectPaddleConfirmed(){
        drawerLayout.closeDrawer(Gravity.LEFT);
        tabListFragment.update_list();
    }

    @Override
    public void menuBarClicked() {
        drawerLayout.openDrawer(Gravity.LEFT);
    }

    @Override
    public void tabSelected(String tag) {
        List<String> a  = new ArrayList<>();
        a.add(tag);
        APIManager.getInstance().setTopics(a);
        Log.d("Logger", "tabSelected");
        newsListFragment.reloadNews();
    }

    @Override
    public void reportCurrent(List<Keywords> selected, List<Keywords> unselected) {
        MainApplication.myUser.selected = selected;
        MainApplication.myUser.unselected = unselected;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("Logger","AppActivity save");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("Logger","AppActivity restore");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Logger","AppActivity start");
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        navView = binding.navView;

        tabs = binding.fragmentContainerup;
        mainArea = binding.fragmentContainer;
        navView.setOnItemSelectedListener(this::onNavItemSelected);
        MainApplication.setNavView(navView);

        mainArea.setLongClickable(true);
        fragmentManager = getSupportFragmentManager();
        selectPaddleFragment = (SelectPaddleFragment) fragmentManager.findFragmentById(R.id.select_paddle);

        tabListFragment = (TabListFragment) fragmentManager.findFragmentByTag("upper_fragment_in_container");
        newsListFragment = (NewsListFragment) fragmentManager.findFragmentByTag("fragment_in_container");

        MainApplication.setTopFragmentContainer(tabs);
        drawerLayout = findViewById(R.id.drawer_layout);

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,Gravity.LEFT);

        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {}
            @Override
            public void onDrawerOpened(View drawerView) {}
            @Override
            public void onDrawerClosed(View drawerView) {
                drawerLayout.setDrawerLockMode(
                        DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.LEFT);
            }
            @Override
            public void onDrawerStateChanged(int newState) {}
        });
    }

    public void replaceFragment(Class<? extends Fragment> fragmentClass) { // 声明: 非原创
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragmentClass, null)
                .addToBackStack(null) //代表支持不同的返回栈
                .commit();
    }

    public boolean onNavItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.navigation_recommend) {
            if(!MainApplication.newsPage) replaceFragment(NewsListFragment.class);
            MainApplication.newsPage = true;
            MainApplication.searchPage = false;
            MainApplication.userPage = false;
            tabs.setVisibility(View.VISIBLE);
            if(MainApplication.newsPageisSearchingPage){
                newsListFragment.reloadNews();
                APIManager.resetAll();
            }
            MainApplication.newsPageisSearchingPage = false;
            return true;
        } else if (item.getItemId() == R.id.navigation_profile) {
            if(! MainApplication.userPage) replaceFragment(UserPageFragment.class);
            MainApplication.newsPage = false;
            MainApplication.searchPage = false;
            MainApplication.userPage = true;
            tabs.setVisibility(View.GONE);
            return true;
        } else if (item.getItemId() == R.id.navigation_newslist){
            if( ! MainApplication.searchPage) replaceFragment(SearchFragment.class);
            MainApplication.newsPage = false;
            MainApplication.searchPage = true;
            MainApplication.userPage = false;
            tabs.setVisibility(View.GONE);
            return true;
        }
        return false;
    }

    @Override
    public void finished() {
        if(!MainApplication.newsPage) replaceFragment(NewsListFragment.class);
        MainApplication.newsPage = true;
        MainApplication.searchPage = false;
        MainApplication.userPage = false;
        MainApplication.newsPageisSearchingPage = true;
        newsListFragment.reloadNews();
    }

    @Override
    public void onBackPressed() {
        if(MainApplication.newsPageisSearchingPage) {
            Log.d("Logger", "back to search");
            MainApplication.newsPageisSearchingPage = false;
            MainApplication.newsPage = false;
            MainApplication.searchPage = true;
            super.onBackPressed();
        } if(MainApplication.newsDetailPage) {
            Log.d("Logger", "back to newslist");
            MainApplication.newsDetailPage = false;
            MainApplication.newsPage = true;
            MainApplication.searchPage = false;
            MainApplication.userPage = false;
            super.onBackPressed();
        }
        else {
            long secondTime = System.currentTimeMillis();
            if(secondTime - firstTime >= 1000) {
                Utils.makeToast(AppActivity.this, "再按一次退出应用");
                firstTime = secondTime;
            } else {
                finish();
                System.exit(0);
            }
        }
//        Intent home = new Intent(Intent.ACTION_MAIN);
//        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        home.addCategory(Intent.CATEGORY_HOME);
//        startActivity(home);
        //        // 在返回键被点击时启动主页
//        Intent intent = new Intent(this, AppActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // 清除其他活动并重新创建主页
//        startActivity(intent);
//        finish(); // 结束当前活动
    }
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(MainApplication.newsDetailPage == true) {
//            return super.onKeyDown(keyCode, event);
//        }
//        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
//            if((System.currentTimeMillis()-exitTime) > 2000){
//                Toast.makeText(getApplicationContext(), "再按一次退出应用", Toast.LENGTH_SHORT).show();
//                exitTime = System.currentTimeMillis();
//            } else {
//                finish();
//                System.exit(0);
//            }
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}