package com.java.wanghaoran.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.java.wanghaoran.R;
import com.java.wanghaoran.Containers.Keywords;

import java.util.List;
import java.util.zip.Inflater;

public class SelectPaddleAdapter  extends BaseAdapter {
    private List<Keywords> list_to_show;
    private Context mContext;
    private boolean isSelected = false;
    private Interface mInterface;

    public SelectPaddleAdapter(Interface listener, List<Keywords> list_to_show, Context mContext){
        this.list_to_show = list_to_show;
        this.mContext = mContext;
        this.mInterface = listener;
    }
    public SelectPaddleAdapter(Interface listener, List<Keywords> list_to_show, Context mContext, boolean isSelected){
        this.list_to_show = list_to_show;
        this.mContext = mContext;
        this.isSelected = isSelected;
        this.mInterface = listener;
    }

    public interface Interface{
        void onChangeSelect(int position, boolean currentStatus);
    }



    @Override
    public int getCount() {
        return list_to_show.size();
    }

    @Override
    public Object getItem(int i) {
        if( i >= 0 && i < list_to_show.size()){
            return list_to_show.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(i < 0 && i > list_to_show.size()){
            return null;
        }
        if(isSelected){
            view = LayoutInflater.from(mContext).inflate(R.layout.subject_box_selected, viewGroup,false);
        }else{
            view = LayoutInflater.from(mContext).inflate(R.layout.subject_box_unselected, viewGroup,false);
        }
        TextView text = view.findViewById(R.id.text_in_subject_box);
        text.setTextSize(28);
        text.setText("  "+list_to_show.get(i).name()+"  ");
        //ViewGroup.LayoutParams layoutParams =  text.getLayoutParams();


        view.setOnClickListener(v ->{
            mInterface.onChangeSelect(i,isSelected);
        });
        return view;
    }
}
