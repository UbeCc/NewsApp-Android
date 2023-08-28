package com.java.wanghaoran.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.java.wanghaoran.MainApplication;
import com.java.wanghaoran.R;
import com.java.wanghaoran.Utils;
import com.java.wanghaoran.containers.News;
import com.java.wanghaoran.service.NewsManager;
import com.java.wanghaoran.service.PictureManager;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {
    private final Fragment fragment;
    private final LayoutInflater inflater;
    private List<News> newsList;
    private Context mainActivity;

    public NewsListAdapter(Fragment fragment, Context context, List<News> newsList) {
        this.fragment = fragment;
        this.inflater = LayoutInflater.from(context);
        this.newsList = newsList;
        this.mainActivity =  MainApplication.getContext();
    }

    /**
     * 获得新闻类型
     * @param position position to query
     * @return 0 -> no image; 1 -> single image; 2 -> double image
     */
    @Override
    public int getItemViewType(int position) {
        return newsList.get(position).getImages().length;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        // viewType 0 -> no image; 1 -> single image; 2 -> double image
        if(viewType == 0) {
            itemView = inflater.inflate(R.layout.news_without_image, parent, false);
        } else if(viewType == 1) {
            itemView = inflater.inflate(R.layout.news_single_image, parent, false);
        } else {
            itemView = inflater.inflate(R.layout.news_double_image, parent, false);
        }
        return new ViewHolder(itemView, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    // 在主页时看到的新闻缩略图，最多有两张图片
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title, description;
        private ImageView picture1, picture2;
        private int type;
        public ViewHolder(View itemView, int type) {
            super(itemView);
            this.type = type;
            if(type == 0) {
                title = itemView.findViewById(R.id.news_no_pic_title);
                description = itemView.findViewById(R.id.news_no_pic_description);
            } else if(type ==1) {
                title = itemView.findViewById(R.id.news_one_pic_title);
                description = itemView.findViewById(R.id.news_one_pic_description);
                picture1 = itemView.findViewById(R.id.news_one_pic_picture_0);
            } else {
                title = itemView.findViewById(R.id.news_two_pic_title);
                description = itemView.findViewById(R.id.news_two_pic_description);
                picture1 = itemView.findViewById(R.id.news_two_pic_picture_0);
                picture2 = itemView.findViewById(R.id.news_two_pic_picture_1);
            }
        }

        /**
         * 绑定数据
         * @param position
         */
        public void bindData(int position) {
            News news = newsList.get(position);
            String mTitle = news.getTitle();

            if(Utils.isIdFromAPI(news.getNewsID())) {
//            if(news.getBeenRead()) {
                this.title.setText(Html.fromHtml("<font color=\"#999999\">" + mTitle + "</font>"));
            } else {
                this.title.setText(mTitle);
            }

            String description = news.getSource() + news.getTime();
            this.description.setText(description);

            if(type != 0) { // 至少有一张图片
                PictureManager.loadPictureWithPlaceHolder(mainActivity, news.getImages()[0], picture1);
            }
            if(type >= 2) { // 有两张图片
                PictureManager.loadPictureWithPlaceHolder(mainActivity, news.getImages()[1], picture2);
            }

            itemView.setOnClickListener(v -> {
                long id = NewsManager.getInstance().createNews(news);
                news.setId(id);
                notifyItemChanged(position);

                Bundle bundle = new Bundle();
                bundle.putLong("newsId",id);
                Utils.replaceFragment(fragment, NewsDetailFragment.class, bundle);
            });
        }
    }
}
