package com.qubicgo.android.appformacion.data.request;

import java.util.List;

public class InvitationSchedulerListRequest {

    private String Status;
    private String Message;
    private List<InvitationSchedulerRequest> body;

    public InvitationSchedulerListRequest() {
    }

    public InvitationSchedulerListRequest(String status, String message, List<InvitationSchedulerRequest> body) {
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

    public List<InvitationSchedulerRequest> getBody() {
        return body;
    }

    public void setBody(List<InvitationSchedulerRequest> body) {
        this.body = body;
    }


}
