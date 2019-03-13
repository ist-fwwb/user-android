package com.huangtao.user.model;

import com.huangtao.user.model.meta.SearchResultType;

public class SearchResultItem {

    SearchResultType type;

    boolean isFirst;

    Meeting meeting;

    MeetingNote note;

    User user;

    MeetingRoom meetingRoom;

    public SearchResultItem(SearchResultType type) {
        this.type = type;
    }

    public SearchResultType getType() {
        return type;
    }

    public void setType(SearchResultType type) {
        this.type = type;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }

    public MeetingNote getNote() {
        return note;
    }

    public void setNote(MeetingNote note) {
        this.note = note;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MeetingRoom getMeetingRoom() {
        return meetingRoom;
    }

    public void setMeetingRoom(MeetingRoom meetingRoom) {
        this.meetingRoom = meetingRoom;
    }
}
