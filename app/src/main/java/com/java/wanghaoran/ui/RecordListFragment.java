package com.java.wanghaoran.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.java.wanghaoran.R;
import com.java.wanghaoran.service.NewsManager;

public class RecordListFragment extends Fragment {
    private RecyclerView recyclerView;
    private NewsListAdapter listAdapter;
    private int mode = 0; // 0 for Favorite, 1 for History;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mode = getArguments().getInt("mode");
        }
        Log.d("record list", "mode" + mode);
    }

    public RecordListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_actionlist, container, false);
        context = view.getContext();
        recyclerView = view.findViewById(R.id.news_list);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setStackFromEnd(true);
        llm.setReverseLayout(true);
        recyclerView.setLayoutManager(llm);
        listAdapter = new NewsListAdapter(this, context, NewsManager.getInstance().getRecord(mode));
        recyclerView.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
        return view;
    }
}