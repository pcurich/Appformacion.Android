package com.qubicgo.android.appformacion.data.request;

import java.io.Serializable;

public class InvitationSchedulerRequest implements Serializable {

    private String date;
    private String timeStart;
    private String timeEnd;
    private String room;
    private String address;
    private String latitude ;
    private String longitude;
    private String url;
    private String dateFormat;
 

    public InvitationSchedulerRequest(String date, String timeStart, String timeEnd, String room, String address, String latitude, String longitude, String url, String dateFormat) {
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.room = room;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.url = url;
        this.dateFormat = dateFormat;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }
}
