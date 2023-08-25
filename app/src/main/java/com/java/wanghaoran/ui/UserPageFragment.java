package com.java.wanghaoran.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.java.wanghaoran.MainApplication;
import com.java.wanghaoran.Utils;
import com.java.wanghaoran.R;
import com.java.wanghaoran.service.NewsManager;


public class UserPageFragment extends Fragment{
    private NewsManager newsManager;
    private Button historyButton;
    private Button favoriteButton;
    private Context context;

    public UserPageFragment(){
        newsManager = NewsManager.getInstance();
    }

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_userpage_new, container, false);

        View collectButton = view.findViewById(R.id.collect_button);
        collectButton.setOnClickListener(v -> {collect_button_click();});

        View commentButton = view.findViewById(R.id.comment_button);
        commentButton.setOnClickListener(v -> {comment_button_click();});

        View lastseeButton = view.findViewById(R.id.lastsee_button);
        lastseeButton.setOnClickListener(v -> {lastsee_button_click();});

        context = view.getContext();
        return view;
    }

    // 这里的mode是0的时候代表收藏，1的时候代表评论，2代表最近浏览
    public void collect_button_click(){
        Toast.makeText(context, "您收藏的新闻", Toast.LENGTH_SHORT);
        Bundle mode = new Bundle();
        mode.putInt("mode", 0);
        Utils.replaceFragment(this, RecordListFragment.class, mode);
    }

    public void comment_button_click(){
        Toast.makeText(context, "您评论过的新闻", Toast.LENGTH_SHORT);
        Bundle mode = new Bundle();
        mode.putInt("mode", 1);
        Utils.replaceFragment(this, RecordListFragment.class, mode);
    }

    public void lastsee_button_click(){
        Toast.makeText(context, "您的最近浏览", Toast.LENGTH_SHORT);
        Bundle mode = new Bundle();
        mode.putInt("mode", 2);
        Utils.replaceFragment(this, RecordListFragment.class, mode);
    }

}