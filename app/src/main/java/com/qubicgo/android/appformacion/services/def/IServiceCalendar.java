package com.qubicgo.android.appformacion.services.def;

import com.qubicgo.android.appformacion.data.request.CalendarListRequest;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IServiceCalendar {

    @POST("{junction}" +"/obtenerCalendario")
    Call<CalendarListRequest> obtenerCalendario(
            @Path(value = "junction", encoded = true) String junction,
            @Header("registro") String registro,
            @Header("ano") String ano,
            @Header("mes") String mes);

}
