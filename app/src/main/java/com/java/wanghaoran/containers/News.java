package com.java.wanghaoran.containers;

import java.util.Objects;

import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class News {
    private long id;
    private String title;
    private String source;
    private String time;
    private String content;
    private String images[];
    private String video[];
//    不要和id搞混，id是新闻在本地仓库的编号
//    newsID是新闻在API中的编号，如2023082700426a95327ebcf446148fc1f28ee846f32c
    private String newsID;
    private String url;
    private boolean isFavorites;
    private boolean beenRead;

    public boolean getIsFavorites() {return isFavorites;}
    public boolean getBeenRead() {return beenRead;}
    @Override
    public String toString() {return new Gson().toJson(this);}

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        News that = (News) obj;
        return (id == that.id) && (newsID.equals(that.newsID));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id + "HashCode" + newsID);
    }
}