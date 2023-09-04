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

import java.lang.reflect.Array;
import java.util.List;
import java.util.Arrays;

public class SelectPaddleAdapter  extends BaseAdapter {
    private boolean buttonShakeStates[] = new boolean[10];
    private List<Keywords> listToShow;
    private Context mContext;
    private boolean isSelected = false;
    private Interface mInterface;

    public SelectPaddleAdapter(Interface listener, List<Keywords> listToShow, Context mContext){
        this.listToShow = listToShow;
        this.mContext = mContext;
        this.mInterface = listener;
        Arrays.fill(this.buttonShakeStates, false);
    }
    public SelectPaddleAdapter(Interface listener, List<Keywords> listToShow, Context mContext, boolean isSelected){
        this.listToShow = listToShow;
        this.mContext = mContext;
        this.isSelected = isSelected;
        this.mInterface = listener;
        Arrays.fill(this.buttonShakeStates, false);
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
    public View getView(int position, View view, ViewGroup viewGroup) {
        if(position < 0 && position > listToShow.size()) {
            return null;
        }
        if(isSelected) {
            view = LayoutInflater.from(mContext).inflate(R.layout.subject_box_selected, viewGroup,false);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.subject_box_unselected, viewGroup,false);
        }
//        YoYo.with(Techniques.Shake)
//                .duration(100)
//                .repeat(1)
//                .playOn(view);
        if (buttonShakeStates[position]) {
            // 如果需要抖动，执行抖动动画
            YoYo.with(Techniques.Shake)
                    .duration(150)
                    .repeat(1)
                    .playOn(view.findViewById(R.id.text_in_subject_box));
            buttonShakeStates[position] = false;
        }
//         设置按钮的点击事件
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击按钮时切换按钮的抖动状态
                buttonShakeStates[position] = !buttonShakeStates[position];

                // 通知适配器数据发生了变化，刷新视图
                notifyDataSetChanged();
            }
        });
        TextView text = view.findViewById(R.id.text_in_subject_box);
        YoYo.with(Techniques.Shake)
                .duration(100)
                .repeat(1)
                .playOn(text);
        Log.d("Logger", text.getText().toString());
        text.setTextSize(25);
        text.setText("  "+listToShow.get(position).name()+"  ");

        view.setOnClickListener(v ->{
            mInterface.onChangeSelect(position, isSelected);
        });
        return view;
    }
}
