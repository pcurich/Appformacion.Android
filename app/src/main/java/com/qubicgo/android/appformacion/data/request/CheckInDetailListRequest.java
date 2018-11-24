package com.qubicgo.android.appformacion.data.request;

import java.util.List;

public class CheckInDetailListRequest {

    private String Status;
    private String Message;
    private List<CheckInDetailRequest> body;

    public CheckInDetailListRequest(String status, String message, List<CheckInDetailRequest> body) {
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

    public List<CheckInDetailRequest> getBody() {
        return body;
    }

    public void setBody(List<CheckInDetailRequest> body) {
        this.body = body;
    }
}
