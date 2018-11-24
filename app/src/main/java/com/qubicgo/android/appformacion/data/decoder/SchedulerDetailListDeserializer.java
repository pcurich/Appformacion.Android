package com.qubicgo.android.appformacion.data.decoder;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.qubicgo.android.appformacion.data.request.SchedulerDetailListRequest;
import com.qubicgo.android.appformacion.data.request.SchedulerDetailRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SchedulerDetailListDeserializer implements JsonDeserializer<SchedulerDetailListRequest> {

    @Override
    public SchedulerDetailListRequest deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        ArrayList<SchedulerDetailRequest> body = new ArrayList<>();

        // Obtain Element
        String  status = (json).getAsJsonObject().get("status").getAsString();
        String  message = "";

        if ( (json).getAsJsonObject().get("message")!=null &&
                !(json).getAsJsonObject().get("message").isJsonNull()) {
            message=(json).getAsJsonObject().get("message").getAsString();
        }

        // Obtain Array
        JsonArray arrayList  = (json).getAsJsonObject().getAsJsonObject("body").getAsJsonArray("actividadesDetalle");

        // for each element of array
        for (JsonElement obj : arrayList) {

            // Object of array
            JsonObject gsonObj = obj.getAsJsonObject();

            // Primitives elements of object
            String date  = gsonObj.get("fecha").getAsString();
            Integer groupPersonId = gsonObj.get("grupoPersonaDetalleId").getAsInt();
            String startTime = gsonObj.get("horaInicio").getAsString();
            String endTime = gsonObj.get("horaFinal").getAsString();
            String room = gsonObj.get("nombre").getAsString();
            String markAssistence = gsonObj.get("marcoAsistencia").getAsString();

            String address = "";
            if(!gsonObj.get("direccion").isJsonNull()){
                address = gsonObj.get("direccion").getAsString();
            }

            body.add(new SchedulerDetailRequest(date,groupPersonId,startTime,endTime,address,room,markAssistence));
        }
        return new SchedulerDetailListRequest(status,message,body);
    }
}
