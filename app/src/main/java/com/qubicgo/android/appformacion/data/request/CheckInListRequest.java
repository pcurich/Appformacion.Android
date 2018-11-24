package com.qubicgo.android.appformacion.data.request;

import java.util.List;

public class CheckInListRequest {

    private String Status;
    private String Message;
    private List<CheckInRequest> body;

    public CheckInListRequest(String status, String message, List<CheckInRequest> body) {
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

    public List<CheckInRequest> getBody() {
        return body;
    }

    public void setBody(List<CheckInRequest> body) {
        this.body = body;
    }
}
