package com.example.worldtest.ui.im.bmob;

import cn.bmob.v3.BmobUser;

public class IMUser extends BmobUser {
//Token属性

    //获取Token的头像地址
    private String tokenPhoto;
    //获取Token的昵称
    private String tokenNickName;

    //基本属性

    //昵称
    private String nickName;
    //头像
    private String photo;

    public String getTokenPhoto() {
        return tokenPhoto;
    }

    public void setTokenPhoto(String tokenPhoto) {
        this.tokenPhoto = tokenPhoto;
    }

    public String getTokenNickName() {
        return tokenNickName;
    }

    public void setTokenNickName(String tokenNickName) {
        this.tokenNickName = tokenNickName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
