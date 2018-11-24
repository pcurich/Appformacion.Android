package com.qubicgo.android.appformacion.data.request;

public class MaterialRequest {
    private String name ;
    private String  type ;
    private String  urlDrive;

    public MaterialRequest(String name, String type, String urlDrive) {
        this.name = name;
        this.type = type;
        this.urlDrive = urlDrive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrlDrive() {
        return urlDrive;
    }

    public void setUrlDrive(String urlDrive) {
        this.urlDrive = urlDrive;
    }
}
