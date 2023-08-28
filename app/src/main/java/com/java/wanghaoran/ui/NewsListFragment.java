package com.java.wanghaoran.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.java.wanghaoran.MainApplication;
import com.java.wanghaoran.R;
import com.java.wanghaoran.Utils;
import com.java.wanghaoran.containers.News;
import com.java.wanghaoran.service.NewsManager;
import com.java.wanghaoran.service.TaskRunner;

import java.util.ArrayList;
import java.util.List;

public class NewsListFragment extends Fragment {
    public static final int PAGESIZE = 10;
    // 需要注意的是这里newsList是List<News>，MainActivity的NewsList是一个视图
    private List<News> newsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NewsListAdapter listAdapter;
    private EndlessRecyclerViewScrollListener listScrollListener;
    private Context context;
    private ProgressBar loadingBar;
    private SwipeRefreshLayout listContainer;
    private int page = 1;
    public static NewsListFragment newsListFragment = new NewsListFragment();
    private static Handler mainHandler = new Handler(Looper.getMainLooper());

    public static NewsListFragment getInstance() {return newsListFragment;}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("NewsList", "onCreateView");

        // 已加载视图，从"新闻列表"或"个人信息"切换过来时调用
        if (MainApplication.NewsList != null) {
            return MainApplication.NewsList;
        }

        View view = inflater.inflate(R.layout.fragment_newslist, container, false);
        context = view.getContext();

        // 添加各种listener和adapter

        listContainer = view.findViewById(R.id.news_list_container);
        // 下拉刷新功能
        listContainer.setOnRefreshListener(this::reloadNews);

        recyclerView = view.findViewById(R.id.news_list);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(llm);
        // 上划加载更多功能，这里调用了EndlessRecycleViewScrollListener
        listScrollListener = new EndlessRecyclerViewScrollListener(llm, (page, totalItemsCount, view1) -> loadNextPage());
        recyclerView.addOnScrollListener(listScrollListener);

        listAdapter = new NewsListAdapter(this, context, newsList);
        recyclerView.setAdapter(listAdapter);

        // 底部的加载条
        loadingBar = view.findViewById(R.id.loading_bar);
        loadingBar.setVisibility(View.VISIBLE);

        reloadNews();
        MainApplication.NewsList = view;
        return view;
    }

    private void loadNextPage() {
        page += 1;
        Log.d("NewsList", "loadNextPage()" + page);
        loadingBar.setVisibility(View.VISIBLE);
        NewsManager.getInstance().getNewsList(PAGESIZE * (page - 1), PAGESIZE, new NewsFetchCallback(false));
    }

    public void reloadNews() {
        page = 1;
        if (listScrollListener == null) return;
        Log.d("NewsList", "reloadNews()");
        // 重新修改
        listScrollListener.resetState();
        NewsManager.getInstance().getNewsList(0, PAGESIZE, new NewsFetchCallback(true));
    }

    // 声明：本逻辑非原创
    public class NewsFetchCallback implements TaskRunner.Callback<List<News>> {
        private final boolean reload;

        public NewsFetchCallback(boolean reload) {this.reload = reload;}

        @Override
        public void complete(TaskRunner.Result<List<News>> res) {
            Log.d("NewsList", "complete executed " + reload + " " + res.isOk() + " " + res.getResult().toString());
//            和前面setVisibility(View.VISIBLE)对应
            loadingBar.setVisibility(View.GONE);
            if (reload) {listContainer.setRefreshing(false);}
            if (res.isOk()) {
                if (reload) {
                    newsList.clear();
                    // 新闻列表是空的，换一个
                    // 正常情况下只会出现在搜索时，直接加载是不会出现的
                    if (res.getResult().size() == 0) {
                        Utils.makeToast(context, "没有搜索到相关新闻\n换个关键词试试吧~");
                    }
                }
                newsList.addAll(res.getResult());
                listAdapter.notifyDataSetChanged();
            } else {
                Utils.makeToast(context, "获取新闻失败");
            }
        }
    }
}