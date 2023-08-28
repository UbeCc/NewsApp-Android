package com.java.wanghaoran.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.AdapterView;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import com.java.wanghaoran.R;
import com.java.wanghaoran.Utils;
import com.java.wanghaoran.containers.Keywords;
import com.java.wanghaoran.service.APIManager;

public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener {
    private List<String> list = new ArrayList<String>();
    private SearchView searchView;
    private Spinner spinnerView;
    private TextView spinnerText;
    private ArrayAdapter<String> spinnerAdapter;
    private EditText startTime, endTime;
    private Button searchButton;
    private String queryText = "";
    private OnSearchInputFinished mListener;
    String mCategory;

    public SearchFragment() {
    }

    public interface OnSearchInputFinished {
        void finished();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SearchFragment.OnSearchInputFinished) {
            mListener = (SearchFragment.OnSearchInputFinished) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list.add("综合");
        for (Keywords keyword : Keywords.values()) {
            list.add(keyword.toString());
        }
    }
    @Override
    public boolean onQueryTextSubmit(String s) {
        queryText = s;
        getInformation();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        queryText = s;
        return false;
    }

    // 搜索效果并不好，实际上还是有很多重复的内容
    private void getInformation() {
        if (queryText == "综合") queryText = "";
        List<String> catagories = new ArrayList<>();
        catagories.add(mCategory);
        String begin_time = Utils.prettifyDateExpression(startTime.getText().toString());
        String end_time = Utils.prettifyDateExpression(endTime.getText().toString());
//        Log.d("SearchFragment", queryText + catagories + begin_time + end_time);
        APIManager.getInstance().setParams(catagories, begin_time, end_time, queryText);
        NewsListFragment.getInstance().reloadNews();
        mListener.finished();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        spinnerText = view.findViewById(R.id.text1);
        spinnerView = view.findViewById(R.id.selections);
//         避免默认选择第一项
        spinnerView.setSelection(0,true);
        spinnerAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, list);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerView.setAdapter(spinnerAdapter);
        spinnerView.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                spinnerText.setText("您现在选择的分类是：" + spinnerAdapter.getItem(arg2));
                mCategory = spinnerAdapter.getItem(arg2);
                arg0.setVisibility(View.VISIBLE);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                arg0.setVisibility(View.VISIBLE);
            }
        });
        spinnerView.setOnTouchListener(new Spinner.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setVisibility(View.INVISIBLE);
                Log.i("spinner", "Spinner Touch事件被触发!");
                return false;
            }
        });

        //焦点改变事件处理
        spinnerView.setOnFocusChangeListener(new Spinner.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                v.setVisibility(View.VISIBLE);
                Log.i("spinner", "Spinner FocusChange事件被触发！");
            }
        });

        searchView = view.findViewById(R.id.searchBar);
        startTime = view.findViewById(R.id.editTextDateStart);
        endTime = view.findViewById(R.id.editTextDateEnd);
        searchButton = view.findViewById(R.id.ssearch_button);
        searchView.setOnQueryTextListener(this);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getInformation();
            }
        });
        return view;
    }
}