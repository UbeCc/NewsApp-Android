package com.java.wanghaoran.containers;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String name = "TestUser";
    private List<Long> read_history = new ArrayList<>();
    private  List<Long> like_history = new ArrayList<>();
    public  List<Keywords> selected = new ArrayList<>();
    public  List<Keywords> unselected = new ArrayList<>();

    public User(){
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
    }
}