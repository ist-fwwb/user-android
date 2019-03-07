package com.huangtao.user.model;

import java.util.List;

public class SearchResult {

    int totalCount;

    List<Meeting> meetings;

    List<MeetingNote> meetingNotes;

    List<User> users;

    List<MeetingRoom> meetingRooms;

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
