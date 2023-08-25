package com.java.wanghaoran.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.coordinatorlayout.widget.ViewGroupUtils;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import androidx.appcompat.widget.SearchView;

import com.java.wanghaoran.R;
import com.java.wanghaoran.Utils;
import com.java.wanghaoran.service.FetchFromAPIManager;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener{

    private SearchView searchView;
    private GridLayout gridLayout;
    private EditText startTime, endTime;
    private Button searchButton;
    private String queryText = "";
    private OnSearchInputFinished mListener;

    public SearchFragment() {}

    public interface OnSearchInputFinished{
        void finished();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SearchFragment.OnSearchInputFinished) {
            mListener = (SearchFragment.OnSearchInputFinished) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    private void collectInformation(){
        Log.d("SearchFragment", queryText );
        List<String > catagories = new ArrayList<>();
        int k = gridLayout.getChildCount();
        for(int i = 0; i < k ; i++){
            CheckBox a = (CheckBox) gridLayout.getChildAt(i);
            if(a.isChecked()){
                catagories.add((String)a.getText());
            }
        }
        String begin_time = Utils.cleanDateExpression(startTime.getText().toString());
        String end_time =  Utils.cleanDateExpression(endTime.getText().toString());

        Log.d("SearchFragment", queryText + catagories + begin_time + end_time);
        FetchFromAPIManager.getInstance().handleSearch(catagories,begin_time,end_time,queryText);
        mListener.finished();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchView = view.findViewById(R.id.searchBar);
        gridLayout = view.findViewById(R.id.selections);
        startTime = view.findViewById(R.id.editTextDateStart);
        endTime = view.findViewById(R.id.editTextDateEnd);
        Log.d("Logger", view.findViewById(R.id.ssearch_button).getClass().toString());
        searchButton = view.findViewById(R.id.ssearch_button);
        searchView.setOnQueryTextListener(this);
//        searchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                collectInformation();
//            }
//        });


        return view;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        queryText = s;
        Log.d("onQueryTextSubmit", s);
        collectInformation();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        queryText = s;
        Log.d("onQueryTextChange", s);
        return false;
    }
}