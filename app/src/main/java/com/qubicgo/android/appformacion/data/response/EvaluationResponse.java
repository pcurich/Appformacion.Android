package com.qubicgo.android.appformacion.data.response;

public class EvaluationResponse {

    private String  message;
    private String  status;
    private boolean isError;

    public EvaluationResponse(String message, String status, boolean isError) {
        this.message = message;
        this.status = status;
        this.isError = isError;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }
}
