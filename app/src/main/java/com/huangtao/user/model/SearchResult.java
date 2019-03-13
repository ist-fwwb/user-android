package com.huangtao.user.model;

import java.util.ArrayList;
import java.util.List;

public class SearchResult {

    int totalCount;

    List<Meeting> meetings = new ArrayList<>();

    List<MeetingNote> meetingNotes = new ArrayList<>();

    List<User> users = new ArrayList<>();

    List<MeetingRoom> meetingRooms = new ArrayList<>();

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<Meeting> getMeetings() {
        return meetings;
    }

    public void setMeetings(List<Meeting> meetings) {
        this.meetings = meetings;
    }

    public List<MeetingNote> getMeetingNotes() {
        return meetingNotes;
    }

    public void setMeetingNotes(List<MeetingNote> meetingNotes) {
        this.meetingNotes = meetingNotes;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<MeetingRoom> getMeetingRooms() {
        return meetingRooms;
    }

    public void setMeetingRooms(List<MeetingRoom> meetingRooms) {
        this.meetingRooms = meetingRooms;
    }
}
