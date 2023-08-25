package com.java.wanghaoran;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.android.material.tabs.TabLayout;
import com.java.wanghaoran.Containers.Keywords;
import com.java.wanghaoran.databinding.ActivityMainBinding;
import com.java.wanghaoran.service.FetchFromAPIManager;
import com.java.wanghaoran.ui.NewsListFragment;
import com.java.wanghaoran.ui.SearchFragment;
import com.java.wanghaoran.ui.SelectPaddleFragment;
import com.java.wanghaoran.ui.TabListFragment;
import com.java.wanghaoran.ui.UserPageFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements TabListFragment.onTabBarListener,
        SelectPaddleFragment.onSelectPaddleListener, SearchFragment.OnSearchInputFinished{

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


    private float mPosX, mPosY, mCurPosX, mCurPosY;
    Boolean newsListVisible = true;


    @Override
    public void selectPaddleConfirmed(){
        drawerLayout.closeDrawer(Gravity.LEFT);

        tabListFragment.update_list();

    }

    @Override
    public void menuBarClicked() {
        Log.d("Mainactivity", "menu clicked");
        drawerLayout.openDrawer(Gravity.LEFT);

    }

    @Override
    public void tabSelected(String tag) {
        Log.d("Mainactivity", "menu tab"+ tag);
        List<String> a  = new ArrayList<>();
        a.add(tag);
        FetchFromAPIManager.getInstance().setSubjects(a);
        newsListFragment.reloadNews();
    }

    @Override
    public void reportCurrent(List<Keywords> selected, List<Keywords> unselected) {
        MainApplication.myUser.selected = selected;
        MainApplication.myUser.unselected = unselected;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {//这个函数来自2022年科协暑培的代码
        super.onCreate(savedInstanceState);
        Log.d("Logger", "test");
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

        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener(){

            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {}

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {}

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                drawerLayout.setDrawerLockMode(
                        DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.LEFT);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

    }


    private void replaceFragment(Class<? extends Fragment> fragmentClass) {//这个函数来自2022年科协暑培的代码
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragmentClass, null)
                .addToBackStack(null) //代表支持不同的返回栈
                .commit();
    }

    private boolean onNavItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.navigation_recommend) {

            if(! MainApplication.newsPage) replaceFragment(NewsListFragment.class);
            MainApplication.newsPage = true;
            MainApplication.searchPage = false;
            MainApplication.userPage = false;

            tabs.setVisibility(View.VISIBLE);

            if(MainApplication.newsPageisSearchingPage){
                newsListFragment.reloadNews();
                FetchFromAPIManager.reset_navi();
            }
            MainApplication.newsPageisSearchingPage = false;
            //  selectPaddle.setVisibility(View.GONE);
            return true;
        } else if (item.getItemId() == R.id.navigation_profile) {
            if(! MainApplication.userPage) replaceFragment(UserPageFragment.class);
            MainApplication.newsPage = false;
            MainApplication.searchPage = false;
            MainApplication.userPage = true;

            //  searchView.setVisibility(View.GONE);
            tabs.setVisibility(View.GONE);
            //  selectPaddle.setVisibility(View.GONE);
            return true;
        } else if (item.getItemId() == R.id.navigation_newslist){
            if( ! MainApplication.searchPage) replaceFragment(SearchFragment.class);
            MainApplication.newsPage = false;
            MainApplication.searchPage = true;
            MainApplication.userPage = false;

            //   searchView.setVisibility(View.VISIBLE);
            tabs.setVisibility(View.GONE);
            //  selectPaddle.setVisibility(View.VISIBLE);
            return true;
        }
        return false;
    }


    @Override
    public void finished() {
        if(! MainApplication.newsPage) replaceFragment(NewsListFragment.class);
        Log.d("finished searching Input", "newsPageisSearchingPage = true");
        MainApplication.newsPage = true;
        MainApplication.searchPage = false;
        MainApplication.userPage = false;
        MainApplication.newsPageisSearchingPage = true;
        newsListFragment.reloadNews();
    }
}