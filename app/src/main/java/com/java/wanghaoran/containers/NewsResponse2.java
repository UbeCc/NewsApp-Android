package com.java.wanghaoran.containers;

import java.util.List;

public class NewsResponse2 {
    public int pageSize;
    public int total;
    public List<NewsContent> data;
    public int currentPage;
    public class NewsContent {
        public String image;
        public String publishTime;
        public String video;
        public String title;
        public String content;
        public String url;
        public String publisher;
        public String newsID;
    }
}