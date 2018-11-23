package com.qubicgo.android.appformacion.data.request;

public class InvitationRequest {

    private String type;
    private String hours;
    private String  minutes;
    private String name;
    private String target;
    private String scheduleId;
    private String groupId;
    private String urlSilabus;
    private String urlDrive;
    private String date;
    private String nSessions;

    public InvitationRequest() {
    }

    public InvitationRequest(String type, String hours, String minutes, String name, String target, String scheduleId, String groupId, String urlSilabus, String urlDrive, String date, String nSessions) {
        this.type = type;
        this.hours = hours;
        this.minutes = minutes;
        this.name = name;
        this.target = target;
        this.scheduleId = scheduleId;
        this.groupId = groupId;
        this.urlSilabus = urlSilabus;
        this.urlDrive = urlDrive;
        this.date = date;
        this.nSessions = nSessions;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUrlSilabus() {
        return urlSilabus;
    }

    public void setUrlSilabus(String urlSilabus) {
        this.urlSilabus = urlSilabus;
    }

    public String getUrlDrive() {
        return urlDrive;
    }

    public void setUrlDrive(String urlDrive) {
        this.urlDrive = urlDrive;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getnSessions() {
        return nSessions;
    }

    public void setnSessions(String nSessions) {
        this.nSessions = nSessions;
    }
}
