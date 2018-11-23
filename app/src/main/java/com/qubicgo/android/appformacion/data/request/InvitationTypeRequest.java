package com.qubicgo.android.appformacion.data.request;

public class InvitationTypeRequest {

    private int flag;
    private String label;
    private String code;

    public InvitationTypeRequest() {
    }

    public InvitationTypeRequest(int flag, String label, String code) {
        this.flag = flag;
        this.label = label;
        this.code = code;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
