package com.qubicgo.android.appformacion.data.request;

public class KeepAlive {
    private String result;
    private boolean status;

    public KeepAlive(String result) {
        this.result = result;
        status = "1".equals(result);
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
