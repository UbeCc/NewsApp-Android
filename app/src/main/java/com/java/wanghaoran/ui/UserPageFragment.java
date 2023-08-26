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
import com.java.wanghaoran.R;
import com.java.wanghaoran.Utils;
import com.java.wanghaoran.service.NewsManager;
import com.java.wanghaoran.ui.RecordListFragment;


public class UserPageFragment extends Fragment {

    private NewsManager newsManager;
    private Button historyButton;
    private Button favoriteButton;
    private Context context;

    public UserPageFragment() {
        newsManager = NewsManager.getInstance();
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,//这个函数来自2022年科协暑培的代码
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_userpage, container, false);
        historyButton = view.findViewById(R.id.history_button);
        favoriteButton = view.findViewById(R.id.favorite_button);
        historyButton.setOnClickListener(v -> {history_button_click();});
        favoriteButton.setOnClickListener(v -> {favorite_button_click();});
        context = view.getContext();

        return view;
    }

    public void history_button_click(){
        Toast.makeText(context,"click history",Toast.LENGTH_SHORT);
        Log.d("history button","click");
        Bundle mode_config = new Bundle();
        mode_config.putInt("mode",0);
        Utils.replaceFragment(this, RecordListFragment.class,mode_config);

    }

    public void favorite_button_click(){
        Toast.makeText(context,"click favorites",Toast.LENGTH_SHORT);
        Log.d("favorite button","click");
        Bundle mode_config = new Bundle();
        mode_config.putInt("mode",1);
        Utils.replaceFragment(this,RecordListFragment.class,mode_config);
    }

}