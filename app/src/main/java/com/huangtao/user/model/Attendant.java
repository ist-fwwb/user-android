package com.huangtao.user.model;

import java.util.Date;

public class Attendant {
    String id;

    String userId;
    Date signInTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getDate() {
        return signInTime;
    }

    public void setDate(Date date) {
        this.signInTime = date;
    }
}
