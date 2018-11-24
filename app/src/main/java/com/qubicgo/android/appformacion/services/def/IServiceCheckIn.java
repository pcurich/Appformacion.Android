package com.qubicgo.android.appformacion.services.def;

import com.qubicgo.android.appformacion.data.request.CheckIn;
import com.qubicgo.android.appformacion.services.implementation.ServiceFactory;
import com.qubicgo.android.appformacion.data.request.CheckInDetailListRequest;
import com.qubicgo.android.appformacion.data.request.CheckInListRequest;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IServiceCheckIn {

    @POST("{junction}" + "/obtenerAsistenciaXMarcar")
    Call<CheckInListRequest> obtenerAsistenciaXMarcar(
            @Path(value = "junction", encoded = true) String junction,
            @Header("registro") String registro);

    @POST("{junction}" + "/marcarAsistencia")
    Call<CheckIn> marcarAsistencia(
            @Path(value = "junction", encoded = true) String junction,
            @Header("grpdId") Integer grpdId,
            @Header("codigoAsistencia") String  codigoAsistencia);


    @POST("{junction}" + "/obtenerAsistenciaXMarcarDetalle")
    Call<CheckInDetailListRequest> obtenerAsistenciaXMarcarDetalle(
            @Path(value = "junction", encoded = true) String junction,
            @Header("registro") String registro,
            @Header("salaId") Integer salaId);




}
