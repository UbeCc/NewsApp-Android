package com.java.wanghaoran.service;
import android.content.SharedPreferences;
import android.util.Log;

import com.java.wanghaoran.MainApplication;
import com.java.wanghaoran.Containers.News;
import com.java.wanghaoran.Containers;
import com.java.wanghaoran.service.FetchFromAPIManager;
import com.java.wanghaoran.Utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class NewsManager {
    private final static NewsManager instance = new NewsManager();
    private static Map<Long, News> news = new HashMap<>();
    private static Map<String, Long> id_convert = new HashMap<>();
    private static Map<Long, String> id_re_convert = new HashMap<>();
    private static List<Long> read_history = new ArrayList<>();
    private static List<Long> favorite_history = new ArrayList<>();
    private static boolean read = false;

    public static void writeFavPreference(){

        SharedPreferences preferences_fav = MainApplication.getContext().getSharedPreferences("fav",0);
        Log.d("preferenceTest","fav");


        preferences_fav.edit().putString("fav",Utils.listToString(favorite_history)).commit();

    }
    public static void writeHisPreference(){
        Log.d("preferenceTest","his");
        SharedPreferences preferences_his = MainApplication.getContext().getSharedPreferences("his",0);
        preferences_his.edit().putString("his",Utils.listToString(read_history)).commit();
    }

    private static void readHistoryFromPreference(){
        Log.d("preferenceTest","read");
        SharedPreferences preferences_his = MainApplication.getContext().getSharedPreferences("his",0);
        SharedPreferences preferences_fav = MainApplication.getContext().getSharedPreferences("fav",0);
        read_history = Utils.stringToList(preferences_his.getString("his",""));
        favorite_history = Utils.stringToList(preferences_fav.getString("fav",""));
        Log.d("preferenceTest","read" + read_history.size() + " " + favorite_history.size());
        for(Long a : favorite_history){
            news.get(a).setFavorites(true);
        }
    }

//    public static void read_from_disk(){
//        List<News> temp =  DBManager.query();
//        for(News item : temp){
//            Long id = Long.valueOf(item.getId());
//            news.put(id, item);
//            id_convert.put(item.getIdFromAPI(),id);
//            id_re_convert.put(id,item.getIdFromAPI());
//        }
//        readHistoryFromPreference();
//        read = true;
//    }

    public static Long convert_id(String API_ID){
//        if(!read)  read_from_disk();
        if(id_convert.containsKey(API_ID)){
            return id_convert.get(API_ID);
        }else{
            return -1L;
        }
    }

    public String convert_id(Long id){
        if(id_re_convert.containsKey(id)){
            return id_re_convert.get(id);
        }else{
            return "";
        }
    }

    public List<News> get_record(int mode){// 0 for history
//        if(!read)  read_from_disk();
         Log.d("NewsManager", "Trying to get news");
        List<News> response = new ArrayList<>();
        if(mode == 0){
            for(Long l : read_history){
                News temp = news.get(l);
                if(temp != null){
                    response.add(temp);
                }
            }
        }else{
            for(Long l : favorite_history){
                News temp = news.get(l);
                if(temp != null){
                    response.add(temp);
                }
            }
        }
        return response;
    }

    public void favorite_trigerred(Long id, boolean like){
//        if(!read)  read_from_disk();
        News operating = news.get(id);
        if(operating == null)return;
        if(operating.getIsFavorites() == false ){
            if(like){
                operating.setFavorites(true);
                favorite_history.add(id);
                Log.d("favourite","like");
            }
        }
        else{
            if(!like) {
                operating.setFavorites(false);
                favorite_history.remove(id);
                Log.d("favourite", "dislike");
            }
        }
        writeFavPreference();
    }

    private Long _createNews(String title, String content, String url, String publisher,
                             String publishTime, String id_from_api,
                             String image_list, String video_list){
        if(id_convert.containsKey(id_from_api)){
            return id_convert.get(id_from_api);
        }
        long id = news.size();
        String images[] = Utils.readStringList(image_list);
        String videos[] = Utils.readStringList(video_list);

        Containers myContainers = new Containers();
        Containers.News a_piece_of_news = myContainers.new News(id, title, publisher, publishTime,
                content,  images, videos, id_from_api, url,false, false);

        news.put(id,a_piece_of_news);
        id_convert.put(id_from_api,id);
        id_re_convert.put(id, id_from_api);
        Log.d("NewsManager::_createNews(raw)",id +" " +id_from_api);
        return id;
    }

    public Long createNews(News a_temp_news){
//        if(!read)  read_from_disk();
        if(id_convert.containsKey(a_temp_news.getIdFromAPI())){
            Long id_ = id_convert.get(a_temp_news.getIdFromAPI());
            read_history.add(id_);
            read_history.remove(id_);
            writeHisPreference();
            return id_;
        }
        long id = news.size();


        News a_new_one  = a_temp_news;
        a_new_one.setId(id);
        a_new_one.setBeenRead(true);
        Log.d("NewsManager::_createNews(from temp news)", a_new_one.toString());
        news.put(id,a_new_one);
        id_convert.put(a_new_one.getIdFromAPI(),id);
        id_re_convert.put(id, a_new_one.getIdFromAPI());
        Log.d("NewsManager::_createNews(from temp news)",id +" " + a_new_one.getIdFromAPI());
        read_history.add(id);
        writeHisPreference();
//        DBManager.add(news);

        return id;
    }


    public void createNews(String title, String content, String url, String publisher,
                           String publishTime, String id_from_api,
                           String image_list, String video_list,
                           TaskRunner.Callback<Long> callback) {
        TaskRunner.getInstance().execute(() -> _createNews(title, content, url, publisher,
                                publishTime, id_from_api, image_list, video_list), callback);
    }


    public void getNews(long id, TaskRunner.Callback<News> callback) {
        TaskRunner.getInstance().execute(() -> Objects.requireNonNull(news.get(id)), callback);
    }

    public News getNews(long id) {
//        if(!read)  read_from_disk();
        return news.get(id);
    }



    public void newsList(int offset, int pageSize, TaskRunner.Callback<List<News>> callback) {
//        if(!read)  read_from_disk();
        TaskRunner.getInstance().execute(() -> {
            ArrayList<News> list = new ArrayList<>(news.values());
            list.sort(Comparator.comparing(News::getId).reversed());
            return list.subList(offset, Math.min(list.size(), offset + pageSize));
//            return applyForNews(offset, pageSize);
        }, callback);
    }


    private NewsManager(){

        //
    }

    private List<News> applyForNews(int offset, int pageSize){
//        if(!read)  read_from_disk();
        return FetchFromAPIManager.getInstance().getNews(offset, pageSize);
    }


    public static NewsManager getInstance() {
        //   if(!read)  read_from_disk();
        return instance;
    }

}
