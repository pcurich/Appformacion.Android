package com.qubicgo.android.appformacion.data.request;

public class SchedulerDetailRequest {

    private String date;
    private Integer groupPersonId;
    private String startTime;
    private String endTime;
    private String address;
    private String room;
    private String markAssistence;

    public SchedulerDetailRequest(String date, Integer groupPersonId, String startTime, String endTime, String address, String room, String markAssistence) {
        this.date = date;
        this.groupPersonId = groupPersonId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.address = address;
        this.room = room;
        this.markAssistence = markAssistence;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getGroupPersonId() {
        return groupPersonId;
    }

    public void setGroupPersonId(Integer groupPersonId) {
        this.groupPersonId = groupPersonId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getMarkAssistence() {
        return markAssistence;
    }

    public void setMarkAssistence(String markAssistence) {
        this.markAssistence = markAssistence;
    }
}
