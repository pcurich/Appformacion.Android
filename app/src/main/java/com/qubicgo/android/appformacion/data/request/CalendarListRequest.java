package com.qubicgo.android.appformacion.data.request;

import java.util.List;

public class CalendarListRequest {

    private String Status;
    private String Message;
    private List<CalendarRequest> body;

    public CalendarListRequest(String status, String message, List<CalendarRequest> body) {
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

    public List<CalendarRequest> getBody() {
        return body;
    }

    public void setBody(List<CalendarRequest> body) {
        this.body = body;
    }
}
