package com.example.worldtest.ui.Report;

import cn.bmob.v3.BmobObject;

public class Report_Moment extends BmobObject {
    private String user;
    private String momentId;
    private String reason;

    @Override
    public String toString() {
        return "Report_Moment{" +
                "user='" + user + '\'' +
                ", momentId='" + momentId + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMomentId() {
        return momentId;
    }

    public void setMomentId(String momentId) {
        this.momentId = momentId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
