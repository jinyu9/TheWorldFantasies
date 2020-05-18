package com.example.worldtest.ui.Report;

import cn.bmob.v3.BmobObject;

public class Report_User extends BmobObject {
    private String user1;
    private String user2;
    private String reason;
    private int state = 0;

    public String getUser1() {
        return user1;
    }

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public String getUser2() {
        return user2;
    }

    public void setUser2(String user2) {
        this.user2 = user2;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Report_User{" +
                "user1='" + user1 + '\'' +
                ", user2='" + user2 + '\'' +
                ", reason='" + reason + '\'' +
                ", state=" + state +
                '}';
    }



}
