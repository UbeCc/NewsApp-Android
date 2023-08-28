package com.java.wanghaoran.ui;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.IntStream;

/**
 * @params visibleThreshold 启动新加载需要的最小数量
 * @params currentPage 当前页数
 * @params previousTotalItemCount 最近一次加载前的总数量
 * @params loading 是否仍等待加载
 * @params startingPageIndex 起始页码
 * @params onLoadMore 加载更多的回调
 * @params mLayoutManager 布局管理器
 */
public class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {

    private int visibleThreshold = 5;
    private int currentPage = 0;
    private int previousTotalItemCount = 0;
    private boolean loading = true;
    private int startingPageIndex = 0;
    private final OnLoadMoreListener onLoadMore;
    RecyclerView.LayoutManager mLayoutManager;

    @FunctionalInterface
    public interface OnLoadMoreListener {
        void onLoadMore(int page, int totalItemsCount, RecyclerView view);
    }

    public EndlessRecyclerViewScrollListener(LinearLayoutManager layoutManager, OnLoadMoreListener onLoadMore) {
        this.mLayoutManager = layoutManager;
        this.onLoadMore = onLoadMore;
    }

    public EndlessRecyclerViewScrollListener(GridLayoutManager layoutManager, OnLoadMoreListener onLoadMore) {
        this.mLayoutManager = layoutManager;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
        this.onLoadMore = onLoadMore;
    }

    public EndlessRecyclerViewScrollListener(StaggeredGridLayoutManager layoutManager, OnLoadMoreListener onLoadMore) {//这个函数来自2022年科协暑培的代码
        this.mLayoutManager = layoutManager;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
        this.onLoadMore = onLoadMore;
    }

    public int getLastVisibleItem(int[] lastVisibleItemPositions) {
        return Arrays.stream(lastVisibleItemPositions).max().getAsInt();
    }

    // 重置状态
    public void resetState() {
        this.currentPage = this.startingPageIndex;
        this.previousTotalItemCount = 0;
        this.loading = true;
    }

    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {
        int lastVisibleItemPosition = 0;
        int totalItemCount = mLayoutManager.getItemCount();

        if (mLayoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(null);
            // 页面可容纳的最大数量
            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
        } else if (mLayoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        } else if (mLayoutManager instanceof LinearLayoutManager) {
            lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        }

        // 如果总数目是0，说明数据已经被清空，需要重置
        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.loading = true;
            }
        }

        // 如果不是正在加载，但是总数目变化了，说明刚刚加载完毕，需要重置
        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
        }

        // 如果不是正在加载，但是已经滑动到了最后一个，说明需要加载更多
        // 如果需要加载更多，那么执行onLoadMore
        // threshold应该反映总列数
        if (!loading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount) {
            currentPage++;
            onLoadMore.onLoadMore(currentPage, totalItemCount, view);
            loading = true;
        }
    }
}