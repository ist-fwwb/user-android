package com.huangtao.user.model;

import java.util.ArrayList;
import java.util.List;

public class TimeSlice {
    String _id;

    String roomId;
    List<String> timeSlice;
    String date;

    public TimeSlice(){
        List<String> occupied = new ArrayList<>();
        // the slices of one day
        int total = 24 * (60 / 15);
        for (int i=0; i<total; ++i) {
            occupied.add(null);
        }
        this.timeSlice = occupied;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public List<String> getTimeSlice() {
        return timeSlice;
    }

    public void setTimeSlice(List<String> timeSlice) {
        this.timeSlice = timeSlice;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
