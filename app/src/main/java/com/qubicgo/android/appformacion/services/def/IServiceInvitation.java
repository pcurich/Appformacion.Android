package com.qubicgo.android.appformacion.services.def;

import com.qubicgo.android.appformacion.services.implementation.ServiceFactory;
import com.qubicgo.android.appformacion.data.request.InvitationListRequest;
import com.qubicgo.android.appformacion.data.request.InvitationSchedulerListRequest;
import com.qubicgo.android.appformacion.data.response.InvitationSendResponse;
import com.qubicgo.android.appformacion.data.request.InvitationTypeListRequest;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IServiceInvitation {

    @POST("{junction}" + "/obtenerInvitacionesActivas")
    Call<InvitationListRequest> obtenerInvitacionesActivas(
            @Path(value = "junction", encoded = true) String junction,
            @Header("registro") String registro);

    @POST("{junction}" + "/obtenerSalaDisponibilidadInvitaciones")
    Call<InvitationSchedulerListRequest> obtenerSalaDisponibilidadInvitaciones(
            @Path(value = "junction", encoded = true) String junction,
            @Header("horarioId") String horarioId);

    @POST("{junction}" + "/obtenerConfiguracionInvitacionActiva")
    Call<InvitationTypeListRequest> obtenerConfiguracionInvitacionActiva(@Path(value = "junction", encoded = true) String junction);

    @POST("{junction}" + "/recepcionRespuestInvitacion")
    Call<InvitationSendResponse> recepcionRespuestInvitacion(
            @Path(value = "junction", encoded = true) String junction,
            @Header("grupoid") String grupoid,
            @Header("registro") String registro,
            @Header("tipoRespuesta") String tipoRespuesta,
            @Header("flag") Integer flag
    );



}
