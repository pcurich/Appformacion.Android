package com.qubicgo.android.appformacion.data.request;

import java.util.List;

public class SchedulerDetailListRequest {

    private String Status;
    private String Message;
    private List<SchedulerDetailRequest> body;

    public SchedulerDetailListRequest(String status, String message, List<SchedulerDetailRequest> body) {
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

    public List<SchedulerDetailRequest> getBody() {
        return body;
    }

    public void setBody(List<SchedulerDetailRequest> body) {
        this.body = body;
    }
}
