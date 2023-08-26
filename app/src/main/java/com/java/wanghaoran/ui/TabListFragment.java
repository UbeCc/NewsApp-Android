package com.java.wanghaoran.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayout.Tab;
import com.java.wanghaoran.MainApplication;
import com.java.wanghaoran.containers.Keywords;
import com.java.wanghaoran.R;
import com.java.wanghaoran.containers.User;

import java.util.ArrayList;
import java.util.List;


public class TabListFragment extends Fragment {
    public List<String> tabs = new ArrayList<String>();
    private View view;
    private TabLayout tabLayout;
    private CheckBox selectMenu;
    private onTabBarListener mListener;

    public TabListFragment() {}
    public interface onTabBarListener {
        void menuBarClicked();
        void tabSelected(String tag);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onTabBarListener) {
            mListener = (onTabBarListener) context;
        } else{
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tablist, container, false);
        tabLayout = view.findViewById(R.id.subject_tabs);
        selectMenu = view.findViewById(R.id.edit_menu);

        selectMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null){
                    mListener.menuBarClicked();
                }
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mListener.tabSelected(tab.getText().toString());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {} // TODO

            @Override
            public void onTabReselected(TabLayout.Tab tab) {} // TODO
        });
        update_list();
        return view;
    }

    public void update_list(){
        Log.d("TabListFragment", "update called");
        tabs.clear();
        tabs.add("综合");
        if(MainApplication.myUser.selected != null){
            for(Keywords a : MainApplication.myUser.selected){
                tabs.add(a.name());
            }
        }
        tabLayout.removeAllTabs();
        for(int i = 0; i < tabs.size(); i++){
            Tab tab = tabLayout.newTab();
            tab.setText(tabs.get(i));
            tabLayout.addTab(tab);
        }
    }
}