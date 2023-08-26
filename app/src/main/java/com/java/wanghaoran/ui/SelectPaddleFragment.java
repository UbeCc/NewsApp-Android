package com.java.wanghaoran.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import com.java.wanghaoran.MainApplication;
import com.java.wanghaoran.R;
import com.java.wanghaoran.containers.User;
import com.java.wanghaoran.containers.Keywords;

import java.util.ArrayList;
import java.util.List;

public class SelectPaddleFragment extends Fragment {

    private List<Keywords> selected = new ArrayList<>();
    private List<Keywords> unselected = new ArrayList<>();

    private SelectPaddleAdapter selected_adapter;
    private SelectPaddleAdapter unselected_adapter;

    private GridView selected_grid_view;
    private GridView unselected_grid_view;

    private SelectPaddleAdapter.Interface listener;
    private Button confirmButton;

    public SelectPaddleFragment() {}

    public List<Keywords> getSelected(){
        return selected;
    }
    public List<Keywords> getUnselected(){
        return unselected;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listener = (new SelectPaddleAdapter.Interface() {
            @Override
            public void onChangeSelect(int position, boolean currentStatus) {
                if(currentStatus){
                    unselected.add(selected.remove(position));
                }else{
                    selected.add(unselected.remove(position));
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

    private onSelectPaddleListener mListener;

    public interface onSelectPaddleListener {
        void reportCurrent(List<Keywords> selected, List<Keywords> unselected);
        void selectPaddleConfirmed();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SelectPaddleFragment.onSelectPaddleListener) {
            mListener = (SelectPaddleFragment.onSelectPaddleListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        try{if(MainApplication.myUser == null) Log.d("Logger", "QWQ is null");}
//        catch(Exception e) {Log.d("Logger", e.toString());}
//        Log.d("Logger", "onCreateView");
//        Log.d("Logger", "null?");
        if (MainApplication.myUser.selected != null && MainApplication.myUser.unselected != null) {
            selected.add(Keywords.教育);
            selected.add(Keywords.娱乐);
            selected.add(Keywords.科技);
            selected.add(Keywords.体育);
            selected.add(Keywords.健康);
            unselected.add(Keywords.军事);
            unselected.add(Keywords.文化);
            unselected.add(Keywords.汽车);
            unselected.add(Keywords.社会);
            unselected.add(Keywords.财经);
        }else{
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
        confirmButton.setOnClickListener(v -> {upload(); mListener.selectPaddleConfirmed();});
        return view;
    }
}

//package com.java.wanghaoran.ui;
//
//import android.content.Context;
//import android.os.Bundle;
//
//import androidx.fragment.app.Fragment;
//
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.GridView;
//
//import com.java.wanghaoran.MainApplication;
//import com.java.wanghaoran.R;
//import com.java.wanghaoran.Containers.User;
//import com.java.wanghaoran.Containers.Keywords;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class SelectPaddleFragment extends Fragment {
//
//    private List<Keywords> selected = new ArrayList<>();
//    private List<Keywords> unselected = new ArrayList<>();
//
//    private SelectPaddleAdapter selected_adapter;
//    private SelectPaddleAdapter unselected_adapter;
//
//    private GridView selected_grid_view;
//    private GridView unselected_grid_view;
//    private SelectPaddleAdapter.Interface listener;
//    private Button confirmButton;
//
//    public SelectPaddleFragment() {
//        // Required empty public constructor
//    }
//
//    public List<Keywords> getSelected(){
//        return selected;
//    }
//
//    public List<Keywords> getUnselected(){
//        return unselected;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        listener = (new SelectPaddleAdapter.Interface() {
//            @Override
//            public void onChangeSelect(int position, boolean currentStatus) {
//                if(currentStatus){
//                    unselected.add(selected.remove(position));
//                }else{
//                    selected.add(unselected.remove(position));
//                }
//                selected_adapter.notifyDataSetChanged();
//                unselected_adapter.notifyDataSetChanged();
//                mListener.reportCurrent(selected, unselected);
//            }
//        });
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        upload();
//        mListener.selectPaddleConfirmed();
//    }
//
//    public void upload(){
//        MainApplication.myUser.selected = this.selected;
//        MainApplication.myUser.unselected = this.unselected;
//    }
//
//    private onSelectPaddleListener mListener;
//
//    public interface onSelectPaddleListener {
//        void reportCurrent(List<Keywords> selected, List<Keywords> unselected);
//        void selectPaddleConfirmed();
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof SelectPaddleFragment.onSelectPaddleListener) {
//            mListener = (SelectPaddleFragment.onSelectPaddleListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        if (MainApplication.myUser.selected != null && MainApplication.myUser.unselected != null) {
//            selected.add(Keywords.教育);
//            selected.add(Keywords.娱乐);
//            selected.add(Keywords.科技);
//            selected.add(Keywords.体育);
//            selected.add(Keywords.健康);
//            unselected.add(Keywords.军事);
//            unselected.add(Keywords.文化);
//            unselected.add(Keywords.汽车);
//            unselected.add(Keywords.社会);
//            unselected.add(Keywords.财经);
//        }else{
//            selected = MainApplication.myUser.selected;
//            unselected = MainApplication.myUser.unselected;
//        }
//
//        // Inflate the layout for this fragment
//        View  view = inflater.inflate(R.layout.fragment_select_paddle, container, false);
//        selected_grid_view = view.findViewById(R.id.selected);
//        unselected_grid_view = view.findViewById(R.id.unselected);
//
//        selected_adapter = new SelectPaddleAdapter(listener ,selected, this.getContext(),true);
//        unselected_adapter = new SelectPaddleAdapter(listener,unselected, this.getContext(),false);
//        selected_grid_view.setAdapter(selected_adapter);
//        unselected_grid_view.setAdapter(unselected_adapter);
//        confirmButton = view.findViewById(R.id.confirm_button);
//        confirmButton.setOnClickListener(v -> {upload();mListener.selectPaddleConfirmed();});
//        return view;
//    }
//}