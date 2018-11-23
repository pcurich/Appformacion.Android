package com.qubicgo.android.appformacion.data.request;

import java.util.List;

public class SchedulerListRequest {

    private String Status;
    private String Message;
    private List<SchedulerRequest> body;

    public SchedulerListRequest() { }

    public SchedulerListRequest(String status, String message, List<SchedulerRequest> body) {
        Status = status;
        Message = message;
        this.body = body;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public List<SchedulerRequest> getBody() {
        return body;
    }

    public void setBody(List<SchedulerRequest> body) {
        this.body = body;
    }
}
