package com.java.wanghaoran.ui;

import static com.java.wanghaoran.Utils.makeToast;
import static com.java.wanghaoran.Utils.replaceFragment;
import static com.java.wanghaoran.ui.NewsListFragment.newsListFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AdapterView;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.appcompat.widget.SearchView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.java.wanghaoran.MainActivity;
import com.java.wanghaoran.MainApplication;
import com.java.wanghaoran.R;
import com.java.wanghaoran.Utils;
import com.java.wanghaoran.AppActivity;
import com.java.wanghaoran.containers.Keywords;
import com.java.wanghaoran.service.APIManager;

public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener {
    private View view;
    private List<String> list = new ArrayList<String>();
    private SearchView searchView;
    private Spinner spinnerView;
    private TextView spinnerText;
    private ArrayAdapter<String> spinnerAdapter;
    private TextView startTime, endTime, startDate, endDate;
    private Button startDateButton;
    private Button startTimeButton;
    private Button endDateButton;
    private Button endTimeButton;
    private DatePickerDialog startDateDialog;
    private DatePickerDialog endDateDialog;
    private TimePickerDialog startTimeDialog;
    private TimePickerDialog endTimeDialog;
    private int startYear, startMonthOfYear, startDayOfMonth, startHourOfDay, startMinute;
    private int endYear, endMonthOfYear, endDayOfMonth, endHourOfDay, endMinute;
    private String queryText = "";
    private OnSearchInputFinished mListener;
    String mCategory;

    public SearchFragment() {}

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
    public void onDetach() {
        super.onDetach();
        Log.d("SearchFragment", "onDetach");
        mListener = null;
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d("SearchFragment", "onResume");
    }
    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d("SearchFragment", "onStop");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("SearchFragment", "onDestroy");
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.d("SearchFragment", "onStart");
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("SearchFragment", "onDestroyView");
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("SearchFragment", "onSaveInstanceState");
    }
    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d("SearchFragment", "onViewStateRestored");
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.d("SearchFragment", "onHiddenChanged");
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.d("SearchFragment", "onLowMemory");
    }
    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
        Log.d("SearchFragment", "onInflate");
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("SearchFragment", "onCreateView");
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
        String begin_time = Utils.prettifyDateExpression(startDate.getText().toString());
        String end_time = Utils.prettifyDateExpression(endDate.getText().toString());
        Log.d("SearchFragment", queryText + catagories + begin_time + end_time);
        APIManager.getInstance().setParams(catagories, begin_time, end_time, queryText);
        NewsListFragment.getInstanceForSearch().reloadNewsForSearch();
        mListener.finished();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);

        spinnerText = view.findViewById(R.id.text1);
        spinnerView = view.findViewById(R.id.selections);
//         避免默认选择第一项
        spinnerView.setSelection(0, true);
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
        startDate = view.findViewById(R.id.startDate);
        startTime = view.findViewById(R.id.startTime);
        endDate = view.findViewById(R.id.endDate);
        endTime = view.findViewById(R.id.endTime);
        Button searchButton = view.findViewById(R.id.ssearch_button);
        searchView.setOnQueryTextListener(this);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getInformation();
            }
        });
        startDateButton = view.findViewById(R.id.start_date_button);
        startTimeButton = view.findViewById(R.id.start_time_button);
        endDateButton = view.findViewById(R.id.end_date_button);
        endTimeButton = view.findViewById(R.id.end_time_button);
        Calendar startCalendar = Calendar.getInstance();
        startYear = startCalendar.get(startCalendar.YEAR);
        startMonthOfYear = startCalendar.get(startCalendar.MONTH);
        startDayOfMonth = startCalendar.get(startCalendar.DAY_OF_MONTH);
        startHourOfDay = startCalendar.get(startCalendar.HOUR_OF_DAY);
        startMinute = startCalendar.get(startCalendar.MINUTE);
        startDateDialog = new DatePickerDialog(this.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker arg0, int year, int monthOfYear,
                                  int dayOfMonth) {
                String text = String.format("%04d年%02d月%02d日", year, monthOfYear + 1, dayOfMonth);
                startDate.setText("您选择的起始日期是：" + text);
            }
        }, startYear, startMonthOfYear, startDayOfMonth);
        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDateDialog.show();
            }
        });
        startTimeDialog = new TimePickerDialog(this.getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(android.widget.TimePicker arg0, int hourOfDay, int minute) {
                String text = String.format("%02d点%02d分", hourOfDay, minute);
                startTime.setText("您选择的起始时间是：" + text);
            }
        }, startHourOfDay, startMinute, true);
        startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // 点击时间选择器按钮时显示出时间对话框
                startTimeDialog.show();
            }
        });

        // End
        Calendar endCalendar = Calendar.getInstance();
        endYear = endCalendar.get(endCalendar.YEAR);
        endMonthOfYear = endCalendar.get(endCalendar.MONTH);
        endDayOfMonth = endCalendar.get(endCalendar.DAY_OF_MONTH);
        endHourOfDay = endCalendar.get(endCalendar.HOUR_OF_DAY);
        endMinute = endCalendar.get(endCalendar.MINUTE);
        endDateDialog = new DatePickerDialog(this.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker arg0, int year, int monthOfYear,
                                  int dayOfMonth) {
                String text = String.format("%04d年%02d月%02d日", year, monthOfYear + 1, dayOfMonth);
                endDate.setText("您选择的结束日期是：" + text);
            }
        }, endYear, endMonthOfYear, endDayOfMonth);
        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endDateDialog.show();
            }
        });
        endTimeDialog = new TimePickerDialog(this.getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(android.widget.TimePicker arg0, int hourOfDay, int minute) {
                String text = String.format("%02d点%02d分", hourOfDay, minute);
                endTime.setText("您选择的结束时间是：" + text);
            }
        }, endHourOfDay, endMinute, true);
        endTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // 点击时间选择器按钮时显示出时间对话框
                endTimeDialog.show();
            }
        });
        return view;
    }
}
