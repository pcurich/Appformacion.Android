package com.qubicgo.android.appformacion.data.request;

import java.util.List;

public class PollActiveListRequest {

    private String Status;
    private String Message;
    private List<PollActiveRequest> body;

    public PollActiveListRequest(String status, String message, List<PollActiveRequest> body) {
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

    public List<PollActiveRequest> getBody() {
        return body;
    }

    public void setBody(List<PollActiveRequest> body) {
        this.body = body;
    }
}
