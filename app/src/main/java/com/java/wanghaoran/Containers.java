package com.java.wanghaoran;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Containers {
    // 新闻列表
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

        public News(long _id, String _title, String _source, String _time, String _content,
                    String _images[], String[] _video, String _idFromAPI, String _url,
                    boolean _isFavorites, boolean _beenRead) {
            this.id = _id;
            this.title = _title;
            this.source = _source;
            this.time = _time;
            this.content = _content;
            this.images = _images;
            this.video = _video;
            this.idFromAPI = _idFromAPI;
            this.url = _url;
            this.isFavorites = _isFavorites;
            this.beenRead = _beenRead;
        }

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

        public String[] getImages() {return images;}
        public String getTitle() {return title;}
        public long getId() {return id;}
        public String getIdFromAPI() {return idFromAPI;}
        public String getSource() {return source;}
        public String getTime() {return time;}
        public boolean getIsFavorites() {return isFavorites;}
        public String getContent() {return content;}
        public String getUrl() {return url;}
        public boolean getBeenRead() {return beenRead;}
        public String[] getVideo() {return video;}


        public void setId(long id) {this.id = id;}
        public void setFavorites(boolean favorites) {isFavorites = favorites;}
        public void setBeenRead(boolean beenRead) {this.beenRead = beenRead;}
    }

    // 候选关键词
    public enum Keywords{ 娱乐, 军事, 教育, 文化, 健康, 财经, 体育, 汽车, 科技, 社会;} // Unicode 可以直接拿中文当变量

    // 新闻响应
    public class NewsResponse {
        public int pageSize;
        public int total;
        public List<NewsContent> data;
        public int currentPage;
        public class NewsContent {
            public String image[];
            public String publishTime;
            public String video;
            public String title;
            public String content;
            public String url;
            public String publisher;
            public String newsID;
        }
    }

    // 用户信息
    public class User {
        private String name = "TestUser";
        private  List<Long> read_history = new ArrayList<>();
        private  List<Long> like_history = new ArrayList<>();
        public  List<Keywords> selected = new ArrayList<>();
        public  List<Keywords> unselected = new ArrayList<>();

        public User(){
            selected.add(Keywords.教育);
            selected.add(Keywords.娱乐);
            selected.add(Keywords.科技);
            selected.add(Keywords.体育);
            selected.add(Keywords.健康);
            unselected.add(Keywords.军事);
            unselected.add(Keywords.文化);
            unselected.add(Keywords.汽车);
            unselected.add(Keywords.社会);
            unselected.add(Keywords.财经);
        }
    }
}
