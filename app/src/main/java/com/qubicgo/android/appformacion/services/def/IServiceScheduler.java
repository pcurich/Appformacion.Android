package com.qubicgo.android.appformacion.services.def;

import com.qubicgo.android.appformacion.services.implementation.ServiceFactory;
import com.qubicgo.android.appformacion.data.request.SchedulerDetailListRequest;
import com.qubicgo.android.appformacion.data.request.SchedulerListRequest;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IServiceScheduler {

    @POST("{junction}" + "/obtenerActividadesProgramadas")
    Call<SchedulerListRequest> obtenerActividadesProgramadas(
            @Path(value = "junction", encoded = true) String junction,
            @Header("registro") String registro);

    @POST("{junction}" + "/obtenerActividadesProgramadasDetalle")
    Call<SchedulerDetailListRequest> obtenerActividadesProgramadasDetalle(
            @Path(value = "junction", encoded = true) String junction,
            @Header("grupoPersonaId") Integer  grupoPersonaId);

}
