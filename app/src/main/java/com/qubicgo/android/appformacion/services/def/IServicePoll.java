package com.qubicgo.android.appformacion.services.def;

import com.qubicgo.android.appformacion.data.request.PollAnswerSettingRequest;
import com.qubicgo.android.appformacion.data.response.PollResponse;
import com.qubicgo.android.appformacion.services.implementation.ServiceFactory;
import com.qubicgo.android.appformacion.data.request.PollActiveListRequest;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IServicePoll {

    @POST("{junction}" + "/buscarEncuestasPendientes")
    Call<PollActiveListRequest> buscarEncuestasPendientes(
            @Path(value = "junction", encoded = true) String junction,
            @Header("registro") String  registro);

    @POST("{junction}" + "/buscarConfiguracionEncuestas")
    Call<PollAnswerSettingRequest> buscarConfiguracionEncuestas(@Path(value = "junction", encoded = true) String junction);

    @POST("{junction}" + "/obtenerRepuestaEncuestas")
    Call<PollResponse> obtenerRepuestaEncuestas(
            @Path(value = "junction", encoded = true) String junction,
            @Header("grupoPersonaId") Integer  grupoPersonaId,
            @Header("scheduleId") Integer scheduleId,
            @Header("expositorId") Integer expositorId,
            @Header("respuestaEncuesta") String respuestaEncuesta);

}
