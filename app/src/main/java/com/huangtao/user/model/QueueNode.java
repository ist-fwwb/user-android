package com.huangtao.user.model;

import com.huangtao.user.model.meta.MeetingRoomUtils;
import com.huangtao.user.model.meta.Size;

import java.util.Set;

public class QueueNode {
    String id;
    String userId;
    String roomId;
    TimeRange timeRange;
    String date;
    Size size;
    Boolean needSignIn;
    String heading;
    String description;
    Set<MeetingRoomUtils> meetingRoomUtilsList;

    public QueueNode(String userId, String roomId, TimeRange timeRange, String date, Size size,
                     Boolean needSignIn, String heading, String description,
                     Set<MeetingRoomUtils> meetingRoomUtilsList) {
        this.userId = userId;
        this.roomId = roomId;
        this.timeRange = timeRange;
        this.date = date;
        this.size = size;
        this.needSignIn = needSignIn;
        this.heading = heading;
        this.description = description;
        this.meetingRoomUtilsList = meetingRoomUtilsList;
    }

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

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public TimeRange getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(TimeRange timeRange) {
        this.timeRange = timeRange;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public Set<MeetingRoomUtils> getMeetingRoomUtilsList() {
        return meetingRoomUtilsList;
    }

    public void setMeetingRoomUtilsList(Set<MeetingRoomUtils> meetingRoomUtilsList) {
        this.meetingRoomUtilsList = meetingRoomUtilsList;
    }

    public Boolean getNeedSignIn() {
        return needSignIn;
    }

    public void setNeedSignIn(Boolean needSignIn) {
        this.needSignIn = needSignIn;
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

    @Override
    public String toString() {
        return "QueueNode{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", roomId='" + roomId + '\'' +
                ", timeRange=" + timeRange +
                '}';
    }
}
