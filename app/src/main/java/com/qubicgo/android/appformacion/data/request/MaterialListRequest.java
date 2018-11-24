package com.qubicgo.android.appformacion.data.request;

import java.util.List;

public class MaterialListRequest {
    private String Status;
    private String Message;

    public MaterialListRequest(String status, String message, List<MaterialRequest> body) {
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

    public List<MaterialRequest> getBody() {
        return body;
    }

    public void setBody(List<MaterialRequest> body) {
        this.body = body;
    }

    private List<MaterialRequest> body;
}
