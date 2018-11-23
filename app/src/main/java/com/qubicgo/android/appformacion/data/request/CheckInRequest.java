package com.qubicgo.android.appformacion.data.request;

public class CheckInRequest {

    private String type;
    private Integer grpdId;
    private Integer roomId;
    private String name;
    private String address;
    private String code;

    public CheckInRequest() {
    }

    public CheckInRequest(String type, Integer grpdId, Integer roomId, String name, String address, String code) {
        this.type = type;
        this.grpdId = grpdId;
        this.roomId = roomId;
        this.name = name;
        this.address = address;
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getGrpdId() {
        return grpdId;
    }

    public void setGrpdId(Integer grpdId) {
        this.grpdId = grpdId;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
