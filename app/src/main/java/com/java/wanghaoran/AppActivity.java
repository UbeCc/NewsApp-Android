package com.java.wanghaoran;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.SearchView;

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
    private ActivityMainBinding binding;
    public BottomNavigationView navView;
    public SearchView searchView;
    public FragmentContainerView mainArea;
    public FragmentContainerView tabs;
    private SelectPaddleFragment selectPaddleFragment;
    private FragmentManager fragmentManager;
    private DrawerLayout drawerLayout;
    private TabListFragment tabListFragment;
    private NewsListFragment newsListFragment;

    Boolean newsListVisible = true;

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
//        Log.d("QWQ", "selected");
        newsListFragment.reloadNews();
    }

    @Override
    public void reportCurrent(List<Keywords> selected, List<Keywords> unselected) {
        MainApplication.myUser.selected = selected;
        MainApplication.myUser.unselected = unselected;
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
            public void onDrawerStateChanged(int newState) {
            }
        });
    }

    private void replaceFragment(Class<? extends Fragment> fragmentClass) { // 声明: 非原创
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragmentClass, null)
                .addToBackStack(null) //代表支持不同的返回栈
                .commit();
    }

    private boolean onNavItemSelected(MenuItem item) {

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
}