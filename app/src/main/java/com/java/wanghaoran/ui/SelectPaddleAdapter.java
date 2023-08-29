package com.java.wanghaoran.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.java.wanghaoran.MainApplication;
import com.java.wanghaoran.R;
import com.java.wanghaoran.containers.Keywords;

import java.util.List;

public class SelectPaddleAdapter  extends BaseAdapter {
    private List<Keywords> listToShow;
    private Context mContext;
    private boolean isSelected = false;
    private Interface mInterface;

    public SelectPaddleAdapter(Interface listener, List<Keywords> listToShow, Context mContext){
        this.listToShow = listToShow;
        this.mContext = mContext;
        this.mInterface = listener;
    }
    public SelectPaddleAdapter(Interface listener, List<Keywords> listToShow, Context mContext, boolean isSelected){
        this.listToShow = listToShow;
        this.mContext = mContext;
        this.isSelected = isSelected;
        this.mInterface = listener;
    }

    public interface Interface{
        void onChangeSelect(int position, boolean currentStatus);
    }

    @Override
    public int getCount() {
        return listToShow.size();
    }

    @Override
    public Object getItem(int i) {
        return (i >= 0 && i < listToShow.size()) ? listToShow.get(i) : null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(i < 0 && i > listToShow.size()) {
            return null;
        }
        if(isSelected) {
            view = LayoutInflater.from(mContext).inflate(R.layout.subject_box_selected, viewGroup,false);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.subject_box_unselected, viewGroup,false);
        }
        YoYo.with(Techniques.Shake)
                .duration(100)
                .repeat(1)
                .playOn(view);
        TextView text = view.findViewById(R.id.text_in_subject_box);
        Log.d("Logger", text.getText().toString());
        text.setTextSize(25);
        text.setText("  "+listToShow.get(i).name()+"  ");

        view.setOnClickListener(v ->{
            mInterface.onChangeSelect(i, isSelected);
        });
        return view;
    }
}
