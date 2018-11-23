package com.qubicgo.android.appformacion.data.request;

import com.google.gson.annotations.SerializedName;

public class DashboardDetailRequest {

    @SerializedName("key")
    private String key;

    @SerializedName("detalle")
    private String detalle;

    @SerializedName("cantidad")
    private int cantidad;

    @SerializedName("goto")
    private String  nextStep;

    public DashboardDetailRequest() { }

    public DashboardDetailRequest(String key, String detalle, int cantidad, String nextStep) {
        this.key = key;
        this.detalle = detalle;
        this.cantidad = cantidad;
        this.nextStep = nextStep;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
