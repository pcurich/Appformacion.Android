package com.qubicgo.android.appformacion.data.request;


import java.io.Serializable;

public class CheckInDetailRequest implements Serializable {

    private String type;
    private String course;
    private String program;
    private String date;
    private String startTime;
    private String endTime;
    private Integer schedulerId;
    private String address;

    public CheckInDetailRequest(String type, String course, String program, String date, String startTime, String endTime, Integer schedulerId, String address) {
        this.type = type;
        this.course = course;
        this.program = program;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.schedulerId = schedulerId;
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public Integer getSchedulerId() {
        return schedulerId;
    }

    public void setSchedulerId(Integer schedulerId) {
        this.schedulerId = schedulerId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
