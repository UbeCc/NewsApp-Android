package com.java.wanghaoran.service;

import android.util.Log;

import com.google.gson.Gson;
import com.java.wanghaoran.containers.News;
import com.java.wanghaoran.containers.NewsResponse;
import com.java.wanghaoran.Utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public final class APIManager {
    private final static APIManager instance = new APIManager();
    private static String startTime = "1970-01-01";
    private static String endTime;
    private static String dateOfToday;
    private static String keyWords = "";
    private static String categories = "";
    private static List<String>  topics;
    private static  List<News> newsFetched = new ArrayList<>();

    public static APIManager getInstance(){return instance;}

    private APIManager(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        dateOfToday = formatter.format(LocalDate.now());
    }

    /**
     * 根据页面执行execute函数，并返回读取新闻列表
     * @param offset 页数
     * @param pageSize 每页新闻数
     * @return newsFetch List<News>
     */
    public List<News> getNews(int offset, int pageSize){
        execute(offset/pageSize + 1);
        Log.d("NewsList", "getNews() " + newsFetched + (offset / pageSize + 1));
        return newsFetched;
    }

    /**
     *
     * 重新设置keyWords, startTime, endTime为默认值
     * keyWords: ""
     * startTime: "1970-01-01"
     * endTime: dateOfToday
     */
    public static void reset(){
        keyWords = "";
        startTime = "1970-01-01";
        endTime = dateOfToday;
    }

    /**
     * 重新设置keyWords, startTime, endTime, topics, categories
     * keyWords: ""
     * startTime: "1970-01-01"
     * endTime: dateOfToday
     * topics: []
     * categories: ""
     */
    public static void  resetAll(){
        reset();
        topics.clear();
        categories = "";
    }

    /**
     * 设定主题
     * @param topics List<String>
     */
    public void setTopics(List<String> topics){
        //不知道为啥闪退
//        resetAll();
//        this.topics = topics;
        reset();
        this.topics = topics;
        this.categories = "";
//除去综合，因为其对应的是空topics
        for(String topic : this.topics){
            if(!topic.equals("综合")){
                this.categories += topic;
                this.categories += ",";
            }
        }
    }

    /**
     * 设定搜索关键词
     * @param topics
     * @param startTime
     * @param endTime
     * @param keyWords
     */
    public void setParams(List<String> topics, String startTime, String endTime, String keyWords){
        setTopics(topics);
        Log.d("API", "categories:" + this.categories + " key = " + keyWords + "startTime =" + startTime + " endTime=" + endTime);
        this.startTime = startTime;
        this.endTime = endTime;
        this.keyWords  = keyWords;
        // MAYBE WRONG
        if(this.endTime.length() < 3) {
            this.endTime = dateOfToday;
        }
    }

    /**
     * 通过OkHTTP获取新闻，并存在APIManager类的newsFetched中
     * @param page 希望读取的页码
     */
    public void execute(int page) {
//      Log.d("Result", keyWords + " " + startTime + " " + endTime + " " + categories);
        String rawUrl = "https://api2.newsminer.net/svc/news/queryNewsList?size=10&startDate="
                + startTime + "&endDate=" + endTime + "&words="+ keyWords + "&categories=" + categories;
        String url = rawUrl + "&page=" + page;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            Gson gson = new Gson();
            NewsResponse newsResponse = gson.fromJson(responseBody, NewsResponse.class);
            Log.d("API", "Successful response");
            if(newsResponse != null) {
                List<NewsResponse.NewsContent> data = newsResponse.data;
                if (!data.isEmpty()) {
                    newsFetched.clear();
                    int newsSize = data.size();
                    Log.d("Result", "QWQ");
                    for (int i = 0; i < newsSize; i++) {
                        String rawTitle = data.get(i).title;
                        String title = rawTitle.split("[\n|\r]")[0];
                        News tempNews = Utils.initNews(title, data.get(i).content, data.get(i).url,
                                data.get(i).publisher, data.get(i).publishTime, data.get(i).newsID,
                                data.get(i).image, data.get(i).video);
                        newsFetched.add(tempNews);
                    }
                }
            } else {
                Log.d("Result", "no response");
            }
        }  catch (Exception e){
            Log.d("NewsList", "Other Error" + e.toString());
        }
    }
}