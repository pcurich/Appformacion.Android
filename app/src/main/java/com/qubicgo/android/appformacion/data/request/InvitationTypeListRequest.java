package com.qubicgo.android.appformacion.data.request;

import java.util.List;

public class InvitationTypeListRequest {

    private String Status;
    private String Message;
    private List<InvitationTypeRequest> body;

    public InvitationTypeListRequest(String status, String message, List<InvitationTypeRequest> body) {
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

    public List<InvitationTypeRequest> getBody() {
        return body;
    }

    public void setBody(List<InvitationTypeRequest> body) {
        this.body = body;
    }
}
