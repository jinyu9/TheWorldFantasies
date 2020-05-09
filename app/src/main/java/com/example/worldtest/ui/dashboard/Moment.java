package com.example.worldtest.ui.dashboard;

import cn.bmob.v3.BmobObject;

public class Moment extends BmobObject {
    private String user_name;
    private String user_avatar; //to be continued
    private String content;
    private String picture;
    //private List<BmobFile> picture;
    //上传多少张图片
    private int n;

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

//    public List<BmobFile> getPicture() {
//        return picture;
//    }
//
//    public void setPicture(List<BmobFile> picture) {
//        this.picture = picture;
//    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

//    public String getPicture() {
//        return picture;
//    }
//
//    public void setPicture(String picture) {
//        this.picture = picture;
//    }

    @Override
    public String toString() {
        return "Moment{" +
                "user_name='" + user_name + '\'' +
                ", user_avatar='" + user_avatar + '\'' +
                ", content='" + content + '\'' +
                ", picture='" + picture + '\'' +
                '}';
    }


}
