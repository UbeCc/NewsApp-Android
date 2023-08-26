package com.java.wanghaoran.containers;

import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

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
    private String idFromAPI;
    private String url;
    private boolean isFavorites;
    private boolean beenRead;

    public boolean getIsFavorites() {return isFavorites;}

    @Override
    public String toString(){
        Gson gson = new Gson();
        String ans = gson.toJson(this);
        return ans;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        News that = (News) o;
        return (id == that.id) && (idFromAPI.equals(that.idFromAPI));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id + "HashCode" + idFromAPI);
    }

}

//package com.java.wanghaoran.containers;
//
//import com.google.gson.Gson;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//
//public class News {
//    private long id;
//    private String title;
//    private String source;
//    private String time;
//    private String content;
//    private String images[];
//    private String video[];
//    private String idFromAPI;
//    private String url;
//    private boolean isFavorites;
//    private boolean beenRead;
//
//    public News(long _id, String _title, String _source, String _time, String _content,
//                String _images[], String _videos[], String _idFromAPI, String _url,
//                boolean _isFavorites, boolean _beenRead) {
//        this.id = _id;
//        this.title = _title;
//        this.source = _source;
//        this.time = _time;
//        this.content = _content;
//        this.images = _images;
//        this.video = _videos;
//        this.idFromAPI = _idFromAPI;
//        this.url = _url;
//        this.isFavorites = _isFavorites;
//        this.beenRead = _beenRead;
//    }
//
//    @Override
//    public String toString(){
//        Gson gson = new Gson();
//        String ans = gson.toJson(this);
//        return ans;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        News that = (News) o;
//        return (id == that.id) && (idFromAPI.equals(that.idFromAPI));
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id + "HashCode" + idFromAPI);
//    }
//
//    public String[] getImages() {return images;}
//    public String getTitle() {return title;}
//    public long getId() {return id;}
//    public String getIdFromAPI() {return idFromAPI;}
//    public String getSource() {return source;}
//    public String getTime() {return time;}
//    public boolean getIsFavorites() {return isFavorites;}
//    public String getContent() {return content;}
//    public String getUrl() {return url;}
//    public boolean getBeenRead() {return beenRead;}
//    public String[] getVideo() {return video;}
//
//
//    public void setId(long id) {this.id = id;}
//    public void setFavorites(boolean favorites) {isFavorites = favorites;}
//    public void setBeenRead(boolean beenRead) {this.beenRead = beenRead;}
//}
