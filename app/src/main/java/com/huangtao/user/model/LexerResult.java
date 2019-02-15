package com.huangtao.user.model;

import com.huangtao.user.model.meta.MeetingRoomUtils;

import java.util.Set;

public class LexerResult {

    int startTime = -1;

    int endTime = -1;

    String date;

    Set<MeetingRoomUtils> utils;

    String heading;

    String description;

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Set<MeetingRoomUtils> getUtils() {
        return utils;
    }

    public void setUtils(Set<MeetingRoomUtils> utils) {
        this.utils = utils;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
