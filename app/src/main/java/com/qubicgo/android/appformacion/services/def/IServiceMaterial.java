package com.qubicgo.android.appformacion.services.def;

import com.qubicgo.android.appformacion.services.implementation.ServiceFactory;
import com.qubicgo.android.appformacion.data.request.MaterialListRequest;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IServiceMaterial {

    @POST("{junction}" + "/obtenerUrlDrive")
    Call<MaterialListRequest> obtenerUrlDrive(
            @Path(value = "junction", encoded = true) String junction,
            @Header("registro") String  registro);

}
