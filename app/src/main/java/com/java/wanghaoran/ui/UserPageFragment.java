package com.java.wanghaoran.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.java.wanghaoran.MainApplication;
import com.java.wanghaoran.R;
import com.java.wanghaoran.Utils;
import com.java.wanghaoran.service.NewsManager;

public class UserPageFragment extends Fragment {

    private NewsManager newsManager;
    private Button historyButton;
    private Button favoriteButton;
    private Context context;
    private TextView showText;

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
        Log.d("Logger", MainApplication.myUser.username);
        historyButton.setText("历史阅读\n共" + NewsManager.history.size() + "条");
        favoriteButton.setText("您的收藏\n共" + NewsManager.favorite.size() + "条");
        showText = view.findViewById(R.id.textView5);
        showText.setText("欢迎您，尊敬的用户\n" + MainApplication.myUser.username);
        Log.d("Logger", MainApplication.myUser.username);
        historyButton.setOnClickListener(v -> {onHistoryButtonClick();});
        favoriteButton.setOnClickListener(v -> {onFavoriteButtonClick();});
        context = view.getContext();
        return view;
    }

    public void onHistoryButtonClick(){
        // 这里必须加show，否则无法展示
        Utils.makeToast(context, "进入历史阅读");
        Log.d("history button","click");
        Bundle mode = new Bundle();
        mode.putInt("mode",0);
        Utils.replaceFragment(this, RecordListFragment.class, mode);
    }

    public void onFavoriteButtonClick(){
        Utils.makeToast(context, "进入您的收藏");
        Log.d("favorite button","click");
        Bundle mode = new Bundle();
        mode.putInt("mode", 1);
        Utils.replaceFragment(this, RecordListFragment.class, mode);
    }
}