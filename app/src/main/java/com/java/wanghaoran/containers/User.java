package com.java.wanghaoran.containers;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class User {
    public String username = "defaultUser";
    private String password = "123456";
    public List<Long> favorite = new ArrayList<>();
    public List<Long> history = new ArrayList<>();
    public  List<Keywords> selected = new ArrayList<>();
    public  List<Keywords> unselected = new ArrayList<>();
    private static List<User> users = new ArrayList<>();
    public static User getUser(String username) {
        for(User user: users) {
            if(user.username.equals(username)) return user;
        }
        return null;
    }

    public static User addUser(String userName, String passWord) {
        User user = new User();
        user.username = userName;
        user.password = passWord;
        users.add(user);
        return user;
    }

    public User(){
        int selectedNum = 0;
        for(Keywords keyword: Keywords.values()) {
            ++selectedNum;
            if(selectedNum <= 5) selected.add(keyword);
            else unselected.add(keyword);
        }
    }
}