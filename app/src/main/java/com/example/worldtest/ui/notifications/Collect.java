package com.example.worldtest.ui.notifications;

import cn.bmob.v3.BmobObject;

public class Collect extends BmobObject {
    private String username;
    private String attractionId;
    private String path0;

    private String chinaName;

    private String englishName;
    private String briefInfor;

    public String getBriefInfor() {
        return briefInfor;
    }

    public String getChinaName() {
        return chinaName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setBriefInfor(String briefInfor) {
        this.briefInfor = briefInfor;
    }

    public void setChinaName(String chinaName) {
        this.chinaName = chinaName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }


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

}
