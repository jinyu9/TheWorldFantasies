package com.example.worldtest.ui.notifications;

import cn.bmob.v3.BmobObject;

public class Collect extends BmobObject {
    private String username;
    private String attractionId;
    private String path0;

    private String show;

    public void setUsername(String username){
        this.username=username;
    }

    public String getUsername(){
        return username;
    }

    public String getAttractionId(){
        return attractionId;
    }

    public void setAttractionId(String attractionId) {
        this.attractionId = attractionId;
    }

    public String getPath0() {
        return path0;
    }

    public void setPath0(String path0) {
        this.path0 = path0;
    }


    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }
}
