package com.qubicgo.android.appformacion.services.def;

import com.qubicgo.android.appformacion.data.request.EnrollDeviceRequest;
import com.qubicgo.android.appformacion.data.request.KeepAlive;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IServiceLogin {

    @POST("pkmslogin.form")
    Call<ResponseBody> login(@Body RequestBody params);

    @POST("pkmslogout.form")
    Call<ResponseBody> logout();

    @POST("{junction}" + "/enrolarDispositivo")
    Call<EnrollDeviceRequest> enrollDevice(
            @Path(value = "junction", encoded = true) String junction,
            @Header("mobileDeviceId") String mobileDeviceId,
            @Header("brand") String brand,
            @Header("model") String model,
            @Header("operativeSystem") String operativeSystem,
            @Header("versionSystem") String versionSystem,
            @Header("tokenPush") String tokenPush,
            @Header("clientId") String clientId);

    @POST("{junction}" + "/pinMessage")
    Call<KeepAlive> pinMessage(
            @Path(value = "junction", encoded = true) String junction
    );

}
