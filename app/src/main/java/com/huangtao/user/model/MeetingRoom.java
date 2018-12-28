package com.huangtao.user.model;

import com.huangtao.user.model.meta.Size;

import java.util.Map;

public class MeetingRoom {
    String _id;

    Map<String, String> utils;
    Size size;
    String location;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Map<String, String> getUtils() {
        return utils;
    }

    public void setUtils(Map<String, String> utils) {
        this.utils = utils;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
