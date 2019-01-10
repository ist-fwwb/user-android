package com.huangtao.user.model;

import com.huangtao.user.model.meta.MeetingType;
import com.huangtao.user.model.meta.Status;

import java.util.List;

public class Meeting {
    String id;

    String heading;
    String description;
    String location;
    int startTime;
    int endTime;
    String hostId;
    List<String> attendants;
    boolean needSignIn;
    String attendantNum;  // a four digit number to attend the meeting
    Status status;
    MeetingType type;

    public String getId() {
        return id;
    }

    public void setId(String _id) {
        this.id = id;
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

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public List<String> getAttendants() {
        return attendants;
    }

    public void setAttendants(List<String> attendants) {
        this.attendants = attendants;
    }

    public boolean isNeedSignIn() {
        return needSignIn;
    }

    public void setNeedSignIn(boolean needSignIn) {
        this.needSignIn = needSignIn;
    }

    public String getAttendantNum() {
        return attendantNum;
    }

    public void setAttendantNum(String attendantNum) {
        this.attendantNum = attendantNum;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

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

    public MeetingType getType() {
        return type;
    }

    public void setType(MeetingType type) {
        this.type = type;
    }
}
