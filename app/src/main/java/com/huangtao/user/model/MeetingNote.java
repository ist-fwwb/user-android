package com.huangtao.user.model;

import com.huangtao.user.model.meta.MeetingNoteType;

import java.util.ArrayList;
import java.util.List;

public class MeetingNote {
    String id;
    MeetingNoteType meetingNoteType;
    String voiceFileName;
    String title;
    String note;
    String meetingId;
    String ownerId;
    List<String> collectorIds = new ArrayList<>();

    public String getVoiceFileName() {
        return voiceFileName;
    }

    public void setVoiceFileName(String voiceFileName) {
        this.voiceFileName = voiceFileName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public List<String> getCollectorIds() {
        return collectorIds;
    }

    public void setCollectorIds(List<String> collectorIds) {
        this.collectorIds = collectorIds;
    }

    public MeetingNoteType getMeetingNoteType() {
        return meetingNoteType;
    }

    public void setMeetingNoteType(MeetingNoteType meetingNoteType) {
        this.meetingNoteType = meetingNoteType;
    }

    public boolean isFavorite(String uid) {
        for (String id : collectorIds) {
            if (id.replaceAll("\"", "").equals(uid)) {
                return true;
            }
        }
        return false;
    }
}
