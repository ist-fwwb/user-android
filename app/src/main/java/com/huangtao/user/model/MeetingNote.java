package com.huangtao.user.model;

import com.huangtao.user.model.meta.MeetingNoteType;

import java.util.HashSet;
import java.util.Set;

public class MeetingNote {
    String id;
    MeetingNoteType meetingNoteType;
    String voiceFileName;
    String title;
    String note;
    String meetingId;
    String ownerId;
    Set<String> collectorIds = new HashSet<>();

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

    public Set<String> getCollectorIds() {
        return collectorIds;
    }

    public void setCollectorIds(Set<String> collectorIds) {
        this.collectorIds = collectorIds;
    }

    public MeetingNoteType getMeetingNoteType() {
        return meetingNoteType;
    }

    public void setMeetingNoteType(MeetingNoteType meetingNoteType) {
        this.meetingNoteType = meetingNoteType;
    }
}
