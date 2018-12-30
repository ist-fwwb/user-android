package com.huangtao.user.model;

import java.util.Date;

public class Attendant {
    String _id;

    String userId;
    Date signInTime;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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
