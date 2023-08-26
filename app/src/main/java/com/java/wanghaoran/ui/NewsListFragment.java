package com.java.wanghaoran.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.java.wanghaoran.MainApplication;
import com.java.wanghaoran.R;
import com.java.wanghaoran.containers.News;
import com.java.wanghaoran.service.NewsManager;
import com.java.wanghaoran.service.TaskRunner;

import java.util.ArrayList;
import java.util.List;

public class NewsListFragment extends Fragment {
    public static final int PAGE_SIZE = 10;
    public static final String LOG_TAG = NewsListFragment.class.getSimpleName();

    private List<News> newsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NewsListAdapter listAdapter;
    private EndlessRecycleViewScrollListener listScrollListener;

    private Context context;
    private ProgressBar loadingBar;
    private SwipeRefreshLayout listContainer;
    private ConstraintLayout mainArea;
    private int page  = 1;
    public static NewsListFragment newsListFragment = new NewsListFragment();

    public static NewsListFragment getInstance() {return newsListFragment;}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("NewsList", "onCreateView");

        if(MainApplication.NewsList != null){
            return MainApplication.NewsList;
        }

        View view = inflater.inflate(R.layout.fragment_newslist, container, false);
        context = view.getContext();

        listContainer = view.findViewById(R.id.news_list_container);
        listContainer.setOnRefreshListener(this::reloadNews);

        recyclerView = view.findViewById(R.id.news_list);

        LinearLayoutManager llm = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(llm);

        listScrollListener = new EndlessRecycleViewScrollListener(llm, (page, totalItemsCount, view1) -> loadNextPage());
        recyclerView.addOnScrollListener(listScrollListener);

        listAdapter = new NewsListAdapter(this, context, newsList);
        recyclerView.setAdapter(listAdapter);

        loadingBar = view.findViewById(R.id.loading_bar);
        loadingBar.setVisibility(View.VISIBLE);

        reloadNews();

        MainApplication.NewsList = view;
        return view;
    }


    private void loadNextPage() {//这个函数来自2022年科协暑培的代码
        page += 1;
        Log.d("NewsListFragment","loadNextPage()" + page);
        loadingBar.setVisibility(View.VISIBLE);
        NewsManager.getInstance().newsList(PAGE_SIZE * (page-1), PAGE_SIZE, new NewsFetchCallback(false));
    }

    public void reloadNews() {//这个函数来自2022年科协暑培的代码
        page = 1;
        if(listScrollListener == null) return;
        Log.d("NewsList","reloadNews()");
        listScrollListener.resetState();
        NewsManager.getInstance().newsList(0, PAGE_SIZE, new NewsFetchCallback(true));
    }

    public class NewsFetchCallback implements TaskRunner.Callback<List<News>> {//这个函数来自2022年科协暑培的代码
        private final boolean reload;
        public NewsFetchCallback(boolean reload) {
            this.reload = reload;
        }//这个函数来自2022年科协暑培的代码

        @Override
        public void complete(TaskRunner.Result<List<News>> res) {//这个函数来自2022年科协暑培的代码
            Log.d("NewsList", "complete executed " + reload + " " + res.isOk() + " " + res.getResult().toString());
            loadingBar.setVisibility(View.GONE);
            if (reload) {
                listContainer.setRefreshing(false);
            }
            if (res.isOk()) {
                if (reload) {
                    newsList.clear();
                }
                newsList.addAll(res.getResult());
                listAdapter.notifyDataSetChanged();
            } else {
                Log.e(LOG_TAG, "Post fetch failed due to exception", res.getError());
            }
        }
    }

}