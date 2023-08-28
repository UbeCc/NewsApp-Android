package com.java.wanghaoran;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

import com.java.wanghaoran.containers.News;
import com.java.wanghaoran.service.NewsManager;

public final class Utils {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);

    /**
     * 用于替换Fragment
     * @param fragment
     * @param fragmentClass
     */
    public static void replaceFragment(Fragment fragment, Class<? extends Fragment> fragmentClass) { // 声明：非原创
        fragment.getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragmentClass, null)
                .addToBackStack(null)
                .commit();
    }

    /**
     * 用于替换Fragment
     * @param fragment
     * @param fragmentClass
     * @param data
     */
    public static void replaceFragment(Fragment fragment, Class<? extends Fragment> fragmentClass, Bundle data) { // 声明：非原创
        fragment.getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragmentClass, data)
                .addToBackStack(null)
                .commit();
    }

    /**
     * 用于读取字符串格式的列表，参考API文档
     * @param stringGotFromAPI
     * @return
     */
    public static String[] readStringList(String stringFromAPI){
        String readingList[] = {};
        if(stringFromAPI.length()>5 && stringFromAPI.indexOf("http") >= 0){
            if(stringFromAPI.charAt(0) == '['){
                stringFromAPI = stringFromAPI.substring(1,stringFromAPI.length()-1);
            }
            System.out.println(stringFromAPI);
            readingList = stringFromAPI.split(",");
            int len = readingList.length;
            for(int i = 0; i < len ; i++){
                String clear = readingList[i];
                if(clear.charAt(0) == ' ')clear = clear.substring(1,clear.length());
                if(clear.charAt(clear.length()-1) == ' ')clear = clear.substring(0,clear.length()-1);
                readingList[i] = clear;
            }
        }
        return readingList;
    }

    /**
     * 用于初始化新闻
     * @param title
     * @param content
     * @param url
     * @param publisher
     * @param publishTime
     * @param idFromAPI
     * @param image_list
     * @param video_list
     * @return
     */
    public static News initNews(String title, String content, String url, String publisher,
                                String publishTime, String idFromAPI,
                                String image_list, String video_list){
        long id = -1;
        String images[] = Utils.readStringList(image_list);
        String videos[] = Utils.readStringList(video_list);
        return new News(id, title, publisher, publishTime,
                content,  images, videos, idFromAPI, url,false, false);
    }

    /**
     * 用于初始化新闻
     * @param title
     * @param content
     * @param url
     * @param publisher
     * @param publishTime
     * @param idFromAPI
     * @param image_list
     * @param video_list
     * @return
     */
    public static News initNews(String title, String content, String url, String publisher,
                                String publishTime, String idFromApi,
                                String image_list[], String video_list){
        long id = -1;
        String videos[] = Utils.readStringList(video_list);
        return new News(id, title, publisher, publishTime,
                content,  image_list, videos,idFromApi, url,false, false);
    }

    /**
     * 用于判断这个是不是API返回的新闻
     * @param idFromAPI
     * @return
     */
    public static boolean isIdFromAPI(String idFromAPI){
        return NewsManager.getInstance().convertIdToNum(idFromAPI) >= 0;
    }

    /**
     * 用于处理日期表达式
     * @param input
     * @return
     */
    public static String prettifyDateExpression(String input){
        StringBuffer a = new StringBuffer();
        int count = 8;
        int length = input.length();
        for(int i = 0; i < length; i++){
            if(input.charAt(i) >= '0' && input.charAt(i) <= '9' && count >= 0){
                a.append(input.charAt(i));
                count--;
                if(count == 4 || count == 2) a.append('-');
            }
        }
        String ans;
        if(count == 0) {
            ans = a.toString();
        } else {
            ans = "";
        }
        return ans;
    }

    /**
     * 用来把List处理成String表达式，用逗号分割
     * @param input List<Long>
     * @return List对应的表达式
     */
    public static String listToString(List<Long> input){
        StringBuffer ans = new StringBuffer();
        for(Long value: input){
            ans.append( value.toString() );
            ans.append(",");
        }
        String ret = new String(ans);
        return ret;
    }

    /**
     * 用来把用逗号分割的String表达式处理成List
     * @param input
     * @return 对应的List<Long>
     */
    public static List<Long> stringToList(String input){
        try{
            String[] temp = input.split(",");
            List<Long> ans = new ArrayList<>();
            for(String sub: temp){
                ans.add(Long.valueOf(sub));
            }
            return ans;}catch (Exception e){
            return new ArrayList<>();
        }
    }

    /**
     * 制作Toast
     * @param context
     * @param text
     */
    public static void makeToast(Context context, String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}