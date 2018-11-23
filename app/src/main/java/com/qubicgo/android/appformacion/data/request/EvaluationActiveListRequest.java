package com.qubicgo.android.appformacion.data.request;

import java.util.List;

public class EvaluationActiveListRequest {

    private String Status;
    private String Message;
    private List<EvaluationActiveRequest> body;

    public EvaluationActiveListRequest(String status, String message, List<EvaluationActiveRequest> body) {
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

    public List<EvaluationActiveRequest> getBody() {
        return body;
    }

    public void setBody(List<EvaluationActiveRequest> body) {
        this.body = body;
    }
}
