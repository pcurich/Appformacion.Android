package com.qubicgo.android.appformacion.data.request;

import java.util.List;

public class InvitationListRequest {

    private String Status;
    private String Message;
    private List<InvitationRequest> body;


    public InvitationListRequest(String status, String message, List<InvitationRequest> body) {
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

    public List<InvitationRequest> getBody() {
        return body;
    }

    public void setBody(List<InvitationRequest> body) {
        this.body = body;
    }
}
