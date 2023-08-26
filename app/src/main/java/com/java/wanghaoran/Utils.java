package com.java.wanghaoran;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.java.wanghaoran.R;
import com.java.wanghaoran.containers.News;
import com.java.wanghaoran.service.NewsManager;

public final class Utils {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);

    public static void replaceFragment(Fragment fragment, Class<? extends Fragment> fragmentClass) {
        fragment.getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragmentClass, null)
                .addToBackStack(null)
                .commit();
    }

    public static void replaceFragment(Fragment fragment, Class<? extends Fragment> fragmentClass, Bundle data) {
        fragment.getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragmentClass, data)
                .addToBackStack(null)
                .commit();
    }

    public static String[] readStringList(String stringGotFromAPI){
        String ans[] = {};
//        stringGotFromAPI = stringGotFromAPI.substring(1, stringGotFromAPI.length()-1);
//        ans = stringGotFromAPI.split(",\\s");
//        return ans;


        if(stringGotFromAPI.length()>5&&stringGotFromAPI.indexOf("http") >= 0){
            if(stringGotFromAPI.charAt(0) == '['){
                stringGotFromAPI = stringGotFromAPI.substring(1,stringGotFromAPI.length()-1);
            }
            System.out.println(stringGotFromAPI);
            ans = stringGotFromAPI.split(",");
            int k = ans.length;
            for(int i = 0; i < k ; i++){
                String clear = ans[i];
                if(clear.charAt(0) == ' ')clear = clear.substring(1,clear.length());
                if(clear.charAt(clear.length()-1) == ' ')clear = clear.substring(0,clear.length()-1);
                ans[i] = clear;
            }

        }
        return ans;

    }

    public static News initNews(String title, String content, String url, String publisher,
                                String publishTime, String id_from_api,
                                String image_list, String video_list){
        long id = -1;
        String images[] = Utils.readStringList(image_list);
        String videos[] = Utils.readStringList(video_list);

        return new News(id, title, publisher, publishTime,
                content,  images, videos,id_from_api, url,false, false);
    }

    public static News initNews(String title, String content, String url, String publisher,
                                String publishTime, String id_from_api,
                                String image_list[], String video_list){
        long id = -1;

        String videos[] = Utils.readStringList(video_list);

        return new News(id, title, publisher, publishTime,
                content,  image_list, videos,id_from_api, url,false, false);
    }

    public static boolean is_an_api_id_read(String id_from_API){
        return NewsManager.getInstance().convert_id(id_from_API) >= 0;
    }

    public static String cleanDateExpression(String input){
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
        if(count == 0){ ans = a.toString();}else{ ans = "";}
        Log.d("cleanDateExpression", "input = " + input + " output = " + ans);
        return ans;

    }

    public static String listToString(List<Long> input){
        StringBuffer ans = new StringBuffer();
        for(Long value : input){
            ans .append( value.toString() );
            ans.append(",");
        }
        String to_return = new String(ans);
        Log.d("utilList_to_string", to_return);
        return to_return;
    }

    public static List<Long> stringToList(String input){
        Log.d("utilStringToList", input);
        try{
            String[] temp = input.split(",");
            List<Long> ans = new ArrayList<>();
            for(String sub : temp){
                // Long value = ;
                ans.add(Long.valueOf(sub));
            }
            return ans;}catch (Exception e){
            return new ArrayList<>();
        }
    }


}