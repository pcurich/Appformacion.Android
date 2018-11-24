package com.qubicgo.android.appformacion.data.request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class DashboardRequest {

    private ArrayList<DashboardDetailRequest> dashboard;

    public DashboardRequest() {
    }

    public DashboardRequest(ArrayList<DashboardDetailRequest> dashboard) {
        this.dashboard = dashboard;
    }

    public ArrayList<DashboardDetailRequest> getDashboard() {
        return dashboard;
    }

    public void setDashboard(ArrayList<DashboardDetailRequest> dashboard) {
        this.dashboard = dashboard;
    }

    public static DashboardDetailRequest parseJSON(String response){
        Gson gson = new GsonBuilder().create();
        DashboardDetailRequest detail = gson.fromJson(response,DashboardDetailRequest.class);
        return detail;
    }
}
