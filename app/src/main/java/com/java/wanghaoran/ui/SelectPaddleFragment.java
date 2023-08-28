package com.java.wanghaoran.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.java.wanghaoran.AppActivity;
import com.java.wanghaoran.LoginActivity;
import com.java.wanghaoran.MainApplication;
import com.java.wanghaoran.R;
import com.java.wanghaoran.Utils;
import com.java.wanghaoran.containers.Keywords;

public class SelectPaddleFragment extends Fragment {
    private List<Keywords> selected = new ArrayList<>();
    private List<Keywords> unselected = new ArrayList<>();
    private SelectPaddleAdapter selected_adapter;
    private SelectPaddleAdapter unselected_adapter;
    private GridView selected_grid_view;
    private GridView unselected_grid_view;
    private SelectPaddleAdapter.Interface listener;
    private Button confirmButton;
    private onSelectPaddleListener mListener;

    public SelectPaddleFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listener = (new SelectPaddleAdapter.Interface() {
            @Override
            public void onChangeSelect(int position, boolean currentStatus) {
                if(currentStatus) {
                    unselected.add(selected.remove(position));
//                    for(int i = 0; i < unselected.size(); i++) {
//                        View childSelectedView = unselected_adapter.getView(i, null, unselected_grid_view);
//                        TextView text = childSelectedView.findViewById(R.id.text_in_subject_box);
//                        YoYo.with(Techniques.Tada)
//                                .duration(100)
//                                .repeat(50)
//                                .playOn(text);
//
//                        YoYo.with(Techniques.Tada)
//                                .duration(100)
//                                .repeat(50)
//                                .playOn(childSelectedView);
//                        Utils.makeToast(getContext(), text.getText().toString());
//                    }
                } else {
                    selected.add(unselected.remove(position));
//                    for(int i = 0; i < selected.size(); i++) {
//                        View childSelected = selected_adapter.getView(i, null, selected_grid_view);
//                        YoYo.with(Techniques.Tada)
//                                .duration(700)
//                                .repeat(5)
//                                .playOn(childSelected);
//                    }
                }
                selected_adapter.notifyDataSetChanged();
                unselected_adapter.notifyDataSetChanged();
                mListener.reportCurrent(selected, unselected);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        upload();
        mListener.selectPaddleConfirmed();
    }

    public void upload(){
        MainApplication.myUser.selected = this.selected;
        MainApplication.myUser.unselected = this.unselected;
    }

    public interface onSelectPaddleListener {
        void reportCurrent(List<Keywords> selected, List<Keywords> unselected);
        void selectPaddleConfirmed();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SelectPaddleFragment.onSelectPaddleListener) {
            mListener = (SelectPaddleFragment.onSelectPaddleListener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (MainApplication.myUser.selected != null && MainApplication.myUser.unselected != null) {
            int selectedNum = 0;
            for(Keywords keyword: Keywords.values()) {
                ++selectedNum;
                if(selectedNum <= 5) selected.add(keyword);
                else unselected.add(keyword);
            }
        } else{
            selected = MainApplication.myUser.selected;
            unselected = MainApplication.myUser.unselected;
        }
//        Log.d("Logger", "onCreateView2");
        View view = inflater.inflate(R.layout.fragment_select_paddle, container, false);
        selected_grid_view = view.findViewById(R.id.selected);
        unselected_grid_view = view.findViewById(R.id.unselected);
        selected_adapter = new SelectPaddleAdapter(listener ,selected, this.getContext(),true);
        unselected_adapter = new SelectPaddleAdapter(listener,unselected, this.getContext(),false);
        selected_grid_view.setAdapter(selected_adapter);
        unselected_grid_view.setAdapter(unselected_adapter);
        confirmButton = view.findViewById(R.id.paddle_confirm_button);
        confirmButton.setOnClickListener(v -> {
            upload();
            mListener.selectPaddleConfirmed();
        });
        return view;
    }
}