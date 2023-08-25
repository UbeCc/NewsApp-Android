package com.java.wanghaoran.service;

import android.util.Log;

import com.google.gson.Gson;
import com.java.wanghaoran.Containers.News;
import com.java.wanghaoran.Containers.NewsResponse;
import com.java.wanghaoran.Utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FetchFromAPIManager {
    private final static FetchFromAPIManager instance = new FetchFromAPIManager();
    private static String dateOfToday;
    private static String start_time = "1970-01-01";
    private static String keyWords = "";
    private static String catagories = "";
    private static String end_time;
    private static List<String>  subjects;

    private static  List<News> temp_read_news = new ArrayList<>();

    private FetchFromAPIManager(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        dateOfToday = formatter.format(LocalDate.now());
    }

    public static void  reset(){
        start_time = "1970-01-01";
        end_time = dateOfToday;
        keyWords = "";
    }

    public static void  reset_navi(){
        start_time = "1970-01-01";
        end_time = dateOfToday;
        keyWords = "";
        subjects.clear();
        catagories ="";
    }


    public void run(int page) {
        String a = "https://api2.newsminer.net/svc/news/queryNewsList?size=10&startDate="+start_time+"&endDate="+end_time+"&words="+ keyWords +"&categories=" + catagories;
        String url = a+"&page="+page;
        //url = "https://api2.newsminer.net/svc/news/queryNewsList?words=%E8%99%9A%E6%8B%9F%E7%8E%B0%E5%AE%9E&size=20&startDate=2021-09-01&endDate=2021-09-02&model=withUrl&websites=%E6%96%B0%E5%8D%8E%E7%BD%91,%E4%BA%BA%E6%B0%91%E7%BD%91";
        Log.d("Trying to get from ",url);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            Log.d("FetchFromAPI", "length of body got = " + responseBody.length() + " from " + url);
            try {
                Gson gson = new Gson();
                NewsResponse newsResponse = gson.fromJson(responseBody, NewsResponse.class);
                Log.d("FetchFromAPI", "Successfully get NewsResponse from " + url);
                if (newsResponse != null) {
                    List<NewsResponse.NewsContent> data = newsResponse.data;
                    if (!data.isEmpty()) {
                        temp_read_news.clear();
                        int k = data.size();
                        Log.d("FetchFromAPI", "length of message read = " + k);
                        for (int i = 0; i < k; i++) {

                            try {
                                String title = data.get(i).title;
                                String clear_title = title.split("\n|\r")[0];

                                News temp_news = Utils.initNews(clear_title, data.get(i).content, data.get(i).url,
                                        data.get(i).publisher, data.get(i).publishTime, data.get(i).newsID,
                                        data.get(i).image, data.get(i).video);

                                temp_read_news.add(temp_news);
                            } catch (Exception e) {
                                continue;
                            }
                        }
                    } else {
                        Log.d("MainActivity", "get 0 news");
                    }
                } else {
                    Log.d("MainActivity", "get null response");
                }
            } catch (Exception e) {
                temp_read_news.clear();
                Gson gson = new Gson();
                NewsResponse newsResponse = gson.fromJson(responseBody, NewsResponse.class);
                Log.d("FetchFromAPI", "(catch)Successfully get NewsResponse from " + url);
                if (newsResponse != null) {
                    List<NewsResponse.NewsContent> data = newsResponse.data;
                    if (!data.isEmpty()) {
                        temp_read_news.clear();
                        int k = data.size();
                        Log.d("FetchFromAPI", "(catch)length of message read = " + k);
                        for (int i = 0; i < k; i++) {

                            try {
                                String title = data.get(i).title;
                                String clear_title = title.split("\n|\r")[0];

                                News temp_news = Utils.initNews(clear_title, data.get(i).content, data.get(i).url,
                                        data.get(i).publisher, data.get(i).publishTime, data.get(i).newsID,
                                        data.get(i).image, data.get(i).video);

                                temp_read_news.add(temp_news);
                            } catch (Exception e2) {
                                continue;
                            }
                        }
                    } else {
                        Log.d("MainActivity", "get 0 news");
                    }
                } else {
                    Log.d("MainActivity", "get null response");
                }

            }
        }  catch (Exception e){
            e.printStackTrace();
        }

    }
    public List<News> getNews(int offset, int pageSize){
        run(offset/pageSize + 1);

        return temp_read_news;
    }

    public void setSubjects(List<String> subjects){
        reset();
        this.subjects = subjects;
        this.catagories = "";
        for(String item : this.subjects){
            if(!item.equals("综合")){
                this.catagories += item ;
                this.catagories += ",";
            }
        }
        Log.d("FetchFromAPI", "categories:" + this.catagories);
    }

    public void handleSearch(List<String> subjects, String beginTime, String endTime, String keyWords){
        reset();
        this.subjects = subjects;
        this.catagories = "";
        for(String item : this.subjects){
            if(!item.equals("综合")){
                this.catagories += item ;
                this.catagories += ",";
            }
        }
        Log.d("FetchFromAPI", "categories:" + this.catagories + " key = " + keyWords + "beginTime =" + beginTime + " endTime=" + endTime);
        this.start_time = beginTime;
        this.end_time = endTime;
        this.keyWords  = keyWords;
        if(this.end_time.length()<3){
            this.end_time = dateOfToday;
        }
    }


    public static FetchFromAPIManager getInstance(){return instance;}
}
