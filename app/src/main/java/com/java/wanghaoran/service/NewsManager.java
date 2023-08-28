package com.java.wanghaoran.service;

import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import com.java.wanghaoran.MainApplication;
import com.java.wanghaoran.containers.News;
import com.java.wanghaoran.Utils;

public final class NewsManager {
    // **TIPS**
    // 对于工具类的非静态方法，可以创建一个静态实例以在其他类中使用
    // 类似C++中在struct MyStruct中声明MyStruct* myStruct
    private final static NewsManager instance = new NewsManager();
    // 如果新闻条目超过int最大值2147483647，需要用long存储
    // 并且方便根据ID查找
    // 所以用Map数据结构
    public static List<Long> favorite = MainApplication.myUser.favorite;
    public static List<Long> history = MainApplication.myUser.history;
    private static Map<Long, News> news = new HashMap<>();
    private static Map<String, Long> idToNumFromAPI = new HashMap<>();
    private static Map<Long, String> idToAPIFromNum = new HashMap<>();
    private static boolean isRead = false;

    private NewsManager(){}

    public static NewsManager getInstance() {return instance;}

    /**
     * 根据ID获取特定一条新闻
     * @param id
     * @return news 一条新闻
     */
    public News getNews(long id) {
        if(!isRead)  readMemory();
        return news.get(id);
    }

    /**
     * 从SQLite中读取历史阅读记录
     */
    public void readMemory(){
        Log.d("News", "readMemory: " + MainApplication.username);
        List<News> memNewsList = DBManager.query();
        for(News memNews : memNewsList) {
            Long id = Long.valueOf(memNews.getId());
            news.put(id, memNews);
            idToNumFromAPI.put(memNews.getNewsID(), id);
            idToAPIFromNum.put(id, memNews.getNewsID());
        }
        readRecord();
        isRead = true;
    }

//    Context.getSharedPreferences的第二个参数mode不是具体数据，而是Operating Mode
//    MODE_PRIVATE,
//    MODE_WORLD_READABLE,
//    MODE_WORLD_WRITEABLE,
//    MODE_MULTI_PROCESS,

    /**
     * 通过SharedPreferences将历史阅读写入SQLite
     */
    public void writeHistory() {
        MainApplication.getInstance().saveUserInfo(MainApplication.username, "HIST", Utils.listToString(history));
    }

    /**
     * 通过SharedPreferences将收藏写入本地而非SQLite
     * 这样可以极大程度上减小查询时间
     */
    public void writeFavorite() {
        MainApplication.getInstance().saveUserInfo(MainApplication.username, "FAVO", Utils.listToString(favorite));
    }

    /**
     * 通过SharedPreferences读取历史阅读和收藏
     */
    private void readRecord() {
        history = Utils.stringToList(MainApplication.getInstance().getUserInfo(MainApplication.username, "HIST"));
        favorite = Utils.stringToList(MainApplication.getInstance().getUserInfo(MainApplication.username, "FAVO"));
        Log.d("Logger", "His & Fav: " + history.toString() + " " + favorite.toString());
//        Log.d("Preference","{History Favorite} size(): "+ history.size() + " " + favorite.size());
        for(Long id: favorite) {
//            Log.d("News", "readRecord: " + favorite.toString());
            news.get(id).setFavorites(true);
        }
    }

    /**
     * 通过API_ID获取本地ID
     * @param idFromAPI
     * @return 本地ID，若没有则返回-1
     */
    public Long convertIdToNum(String idFromAPI) {
        if(!isRead)  readMemory();
        if(idToNumFromAPI.containsKey(idFromAPI)) {
            return idToNumFromAPI.get(idFromAPI);
        } else {
            return -1l;
        }
    }

    /**
     * 通过本地ID获取历史或收藏的全部新闻
     * @param mode 0 for History, 1 for Favorite
     * @return List<News>，若没有则返回null
     */
    public List<News> getRecord(int mode){
        if(!isRead) readMemory();
        List<News> responseNews = new ArrayList<>();
        if(mode == 0) {
            Log.d("News", history.toString() + "HISTORY");
            for(Long l: history) {
                News tempNews = news.get(l);
                if(tempNews != null) {
                    responseNews.add(tempNews);
                }
            }
        } else if(mode == 1) {
            Log.d("News", favorite.toString() + "FAVORITE");
            for(Long l: favorite) {
                News tempNews = news.get(l);
                if(tempNews != null) {
                    responseNews.add(tempNews);
                }
            }
        }
        return responseNews;
    }

    /**
     * 修改对应id的新闻的收藏状态
     * @param id 新闻的本地ID
     * @param isLike true for like, false for dislike
     */
    public void favoriteSelected(Long id, boolean isLike) {
        if(!isRead) readMemory();
        News mNews = news.get(id);
        if(mNews == null) return; // 没有这一条新闻
        if(mNews.getIsFavorites() == false) {
            if(isLike) {
                mNews.setFavorites(true);
                favorite.add(id);
                Log.d("favourite","like");
            }
        } else {
            if(!isLike) {
                mNews.setFavorites(false);
                favorite.remove(id);
                Log.d("favourite", "dislike");
            }
        }
        writeFavorite();
    }

    /**
     * 向历史记录中添加新闻
     * @param mNews News，要添加的新闻
     * @return API_ID，若没有则返回null
     */
    public Long createNews(News mNews) {
        Long id;
        if(!isRead)  readMemory();
        if(idToNumFromAPI.containsKey(mNews.getNewsID())){
            id = idToNumFromAPI.get(mNews.getNewsID());
            history.add(id);
            history.remove(id);
            writeHistory();
            return id;
        }

        id = news.size() + 0L;

        News newNews = mNews;
        newNews.setId(id);
        newNews.setBeenRead(true);
        news.put(id, newNews);
        idToNumFromAPI.put(newNews.getNewsID(),id);
        idToAPIFromNum.put(id, newNews.getNewsID());
        history.add(id);
        writeHistory();
        DBManager.add(news);

        return id;
    }

    public void getNewsList(int offset, int pageSize, TaskRunner.Callback<List<News>> callback) {
        if(!isRead)  readMemory();
        TaskRunner.getInstance().execute(new Callable<List<News> >(){
        @Override
        public List<News> call(){
            return executeNews(offset, pageSize);}
        }, callback);
    }

    private List<News> executeNews(int offset, int pageSize) {
        Log.d("NewsList", "executeNews executed");
        if(!isRead)  readMemory();
        return APIManager.getInstance().getNews(offset, pageSize);
    }
}

