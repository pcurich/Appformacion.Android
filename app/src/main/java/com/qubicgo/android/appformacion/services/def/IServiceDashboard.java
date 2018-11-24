package com.qubicgo.android.appformacion.services.def;


import com.qubicgo.android.appformacion.services.implementation.ServiceFactory;
import com.qubicgo.android.appformacion.data.request.DashboardRequest;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IServiceDashboard {

    @POST("{junction}" + "/obtenerDashboarInfo")
    Call<DashboardRequest> getDashboard(
            @Path(value = "junction", encoded = true) String junction,
            @Header("registro") String registro);
}
