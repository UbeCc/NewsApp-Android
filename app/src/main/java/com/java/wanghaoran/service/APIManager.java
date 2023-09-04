package com.java.wanghaoran.service;

import android.util.Log;

import com.java.wanghaoran.containers.News;
import com.java.wanghaoran.Utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
     * 通过JsonObject+URLConnect获取新闻，并存在APIManager类的newsFetched中
     * @param page 希望读取的页码
     */
    public void execute(int page) {
        try {
            String rawUrl = "https://api2.newsminer.net/svc/news/queryNewsList?size=15&startDate="
                    + startTime + "&endDate=" + endTime + "&words="+ keyWords + "&categories=" + categories;
            String url = rawUrl + "&page=" + page;
            // 创建 URL 对象
            URL apiUrl = new URL(url);
            // 打开连接
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("GET");
            // 获取响应码
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 读取响应数据
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                // 解析 JSON 响应
                JSONObject jsonResponse = new JSONObject(response.toString());
                if (jsonResponse.has("data")) {
                    JSONArray jsonDataArray = jsonResponse.getJSONArray("data");
                    // 清空newsFetched
                    newsFetched.clear();
                    for (int i = 0; i < jsonDataArray.length(); i++) {
                        JSONObject newsObject = jsonDataArray.getJSONObject(i);
                        String rawTitle = newsObject.getString("title");
                        String title = rawTitle.split("[\n|\r]")[0];
                        News tempNews = Utils.initNews(title, newsObject.getString("content"), newsObject.getString("url"),
                                newsObject.getString("publisher"), newsObject.getString("publishTime"),
                                newsObject.getString("newsID"), newsObject.getString("image"), newsObject.getString("video"));
                        newsFetched.add(tempNews);
                    }
                } else {
                    Log.d("Result", "no data in response");
                }
            } else {
                Log.d("Result", "HTTP Error: " + responseCode);
            }
            // 关闭连接
            connection.disconnect();
        } catch (Exception e) {
            Log.d("NewsList", "Other Error" + e.toString());
        }
    }
}