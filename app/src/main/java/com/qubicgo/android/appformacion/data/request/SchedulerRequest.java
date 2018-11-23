package com.qubicgo.android.appformacion.data.request;

public class SchedulerRequest {

    private String type;
    private String target;
    private String name;
    private String sessions;
    private String urlDrive;
    private String pdfUrl;
    private Integer groupPersonId;
    private String hashTag;

    public SchedulerRequest() { }

    public SchedulerRequest(String type, String target, String name, String sessions, String urlDrive, String pdfUrl, Integer groupPersonId, String hashTag) {
        this.type = type;
        this.target = target;
        this.name = name;
        this.sessions = sessions;
        this.urlDrive = urlDrive;
        this.pdfUrl = pdfUrl;
        this.groupPersonId = groupPersonId;
        this.hashTag = hashTag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSessions() {
        return sessions;
    }

    public void setSessions(String sessions) {
        this.sessions = sessions;
    }

    public String getUrlDrive() {
        return urlDrive;
    }

    public void setUrlDrive(String urlDrive) {
        this.urlDrive = urlDrive;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public Integer getGroupPersonId() {
        return groupPersonId;
    }

    public void setGroupPersonId(Integer groupPersonId) {
        this.groupPersonId = groupPersonId;
    }

    public String getHashTag() {
        return hashTag;
    }

    public void setHashTag(String hashTag) {
        this.hashTag = hashTag;
    }
}
