package com.qubicgo.android.appformacion.services.def;

import com.qubicgo.android.appformacion.data.response.EvaluationResponse;
import com.qubicgo.android.appformacion.services.implementation.ServiceFactory;
import com.qubicgo.android.appformacion.data.request.EvaluationActiveListRequest;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface  IServiceEvaluation {

    @POST("{junction}" + "/obtenerEvaluaciones")
    Call<EvaluationActiveListRequest> obtenerEvaluaciones(
            @Path(value = "junction", encoded = true) String junction,
            @Header("registro") String  registro);

    @POST("{junction}" + "/recepcionRespuestaEvaluacion")
    Call<EvaluationResponse> recepcionRespuestaEvaluacion(
            @Path(value = "junction", encoded = true) String junction,
            @Header("grppId") Integer  grppId,
            @Header("respuestaEvaluacion") String respuestaEvaluacion);

}
