package com.java.wanghaoran.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.java.wanghaoran.MainApplication;
import com.java.wanghaoran.R;
import com.java.wanghaoran.containers.News;
import com.java.wanghaoran.service.NewsManager;
import com.java.wanghaoran.service.PictureManager;
import com.java.wanghaoran.Utils;

public class NewsDetailFragment extends Fragment {
    private Context context;
    private News newsToShow;
    private Long newsID = -1L;
    private TextView newsTitle, newsDescription, newsContent;
    private ImageView newsImage1, newsImage2, newsImage3, newsImage4;
    private FloatingActionButton buttonFavorite, buttonUnFavorite;
    private FragmentContainerView containerView;

    /**
     * 处理收藏按钮的点击事件
     * @param mode 0 -> favorite, 1 -> unfavorite
     */
    private void favoriteClicked(int mode) {
        Utils.makeToast(context, "收藏成功");
        if(mode == 0){
            NewsManager.getInstance().favoriteSelected(newsID,true);
            buttonUnFavorite.setVisibility(View.VISIBLE);
            buttonFavorite.setVisibility(View.GONE);
        } else {
            NewsManager.getInstance().favoriteSelected(newsID,false);
            buttonUnFavorite.setVisibility(View.VISIBLE);
            buttonFavorite.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MainApplication.getNavView().setVisibility(View.GONE);
        MainApplication.getTopFragmentContainer().setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("Logger", "NewsDetailStart");
        View view = inflater.inflate(R.layout.newsreading_single_image, container, false);
        if(getArguments() != null) {
            newsID = getArguments().getLong("newsId",-1);
        }
        if(newsID >= 0) {
            newsToShow =  NewsManager.getInstance().getNews(newsID);
        }else {
            newsToShow = Utils.initNews("Error","","","","","","","");
        }

        context = view.getContext();
        newsTitle =  view.findViewById(R.id.detail_title);
        newsDescription = view.findViewById(R.id.detail_description);
        newsContent = view.findViewById(R.id.detail_content);
        newsImage1 = view.findViewById(R.id.detail_image1);
        newsImage2 = view.findViewById(R.id.detail_image2);
        newsImage3 = view.findViewById(R.id.detail_image3);
        newsImage4 = view.findViewById(R.id.detail_image4);
        containerView = view.findViewById(R.id.fragment_videocontainer);
        buttonFavorite = view.findViewById(R.id.favoriteFloatingActionButton);
        buttonUnFavorite = view.findViewById(R.id.favoriteFloatingActionButton2);

        newsTitle.setText(newsToShow.getTitle());
        newsDescription.setText(newsToShow.getSource() + "    " +newsToShow.getTime());
        newsContent.setText(newsToShow.getContent());

        buttonFavorite.setOnClickListener(v->{favoriteClicked(0);});
        buttonUnFavorite.setOnClickListener(v->{favoriteClicked(1);});

        if(!newsToShow.getIsFavorites()){
            buttonUnFavorite.setVisibility(View.GONE);
        } else {
            buttonFavorite.setVisibility(View.GONE);
        }

        // 最多容纳四张照片，这里没有搞SwipeRefreshLayout。因为据观察一条新闻最多四张照片
        if(newsToShow.getImages().length >= 1 ) PictureManager.loadPictureWithoutPlaceHolder(
                context,newsToShow.getImages()[0], newsImage1);
        else newsImage1.setVisibility(View.GONE);
        if(newsToShow.getImages().length >= 2 ) PictureManager.loadPictureWithoutPlaceHolder(
                context,newsToShow.getImages()[1],newsImage2);
        else newsImage2.setVisibility(View.GONE);
        if(newsToShow.getImages().length >= 3 ) PictureManager.loadPictureWithoutPlaceHolder(
                context,newsToShow.getImages()[2],newsImage3);
        else newsImage3.setVisibility(View.GONE);
        if(newsToShow.getImages().length >= 4 ) PictureManager.loadPictureWithoutPlaceHolder(
                context,newsToShow.getImages()[3],newsImage4);
        else newsImage4.setVisibility(View.GONE);

        if(newsToShow.getVideo().length != 0) {
            String path = newsToShow.getVideo()[0];
            VideoFragment to_fill = VideoFragment.getInstance(path);
            getParentFragmentManager().beginTransaction().add(R.id.fragment_videocontainer, to_fill).commit();
//
//            VideoView videoView = view.findViewById(R.id.fragment_to_contain_video).findViewById(R.id.videoView);
//            videoView.setVideoPath("http://live.hkstv.hk.lxdns.com/live/hks/playlist.m3u8");  // 香港卫视地址
//            videoView.start();
//            MediaController mediaController = new MediaController(context);
//            videoView.setMediaController(mediaController);
//            mediaController.setMediaPlayer(videoView);
        } else containerView.setVisibility(View.GONE);

        MainApplication.getNavView().setVisibility(View.GONE);
        MainApplication.getTopFragmentContainer().setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        MainApplication.getNavView().setVisibility(View.VISIBLE);
        if(MainApplication.newsPage && !MainApplication.newsPageisSearchingPage) {
            MainApplication.getTopFragmentContainer().setVisibility(View.VISIBLE);
        }
    }
}