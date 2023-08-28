package com.java.wanghaoran.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.java.wanghaoran.R;
import com.java.wanghaoran.Utils;
import com.java.wanghaoran.service.NewsManager;

public class UserPageFragment extends Fragment {

    private NewsManager newsManager;
    private Button historyButton;
    private Button favoriteButton;
    private Context context;

    public UserPageFragment() {
        newsManager = NewsManager.getInstance();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_userpage, container, false);
        historyButton = view.findViewById(R.id.history_button);
        favoriteButton = view.findViewById(R.id.favorite_button);
        historyButton.setOnClickListener(v -> {history_button_click();});
        favoriteButton.setOnClickListener(v -> {favorite_button_click();});
        context = view.getContext();
        return view;
    }

    public void history_button_click(){
        // 这里必须加show，否则无法展示
        Utils.makeToast(context, "进入历史阅读");
        Log.d("history button","click");
        Bundle mode = new Bundle();
        mode.putInt("mode",0);
        Utils.replaceFragment(this, RecordListFragment.class, mode);
    }

    public void favorite_button_click(){
        Utils.makeToast(context, "进入您的收藏");
        Log.d("favorite button","click");
        Bundle mode = new Bundle();
        mode.putInt("mode", 1);
        Utils.replaceFragment(this, RecordListFragment.class, mode);
    }
}