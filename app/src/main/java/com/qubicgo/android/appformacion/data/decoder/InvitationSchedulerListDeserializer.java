package com.qubicgo.android.appformacion.data.decoder;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.qubicgo.android.appformacion.data.request.InvitationSchedulerListRequest;
import com.qubicgo.android.appformacion.data.request.InvitationSchedulerRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class InvitationSchedulerListDeserializer implements JsonDeserializer<InvitationSchedulerListRequest> {

    @Override
    public InvitationSchedulerListRequest deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        ArrayList<InvitationSchedulerRequest> body = new ArrayList<>();

        // Obtain Element
        String  status = (json).getAsJsonObject().get("status").getAsString();
        String  message = "";

        if ( (json).getAsJsonObject().get("message")!=null &&
                !(json).getAsJsonObject().get("message").isJsonNull()) {
            message=(json).getAsJsonObject().get("message").getAsString();
        }

        // Obtain Array
        JsonArray arrayList  = (json).getAsJsonObject().getAsJsonObject("body").getAsJsonArray("cursoDetalle");


        // for each element of array
        for (JsonElement obj : arrayList) {

            // Object of array
            JsonObject gsonObj = obj.getAsJsonObject();

            // Primitives elements of object
            String date  = gsonObj.get("fecha").getAsString();
            String timeEnd = gsonObj.get("horaFinal").getAsString();
            String timeStart = gsonObj.get("horaInicio").getAsString();
            String room = gsonObj.get("sala").getAsString();
            String dateFormat = gsonObj.get("fechaInicioFormato").getAsString();

            String latitude = "";
            if(!gsonObj.get("latitud").isJsonNull()){
                latitude = gsonObj.get("latitud").getAsString();
            }

            String longitude = "";
            if(!gsonObj.get("longitud").isJsonNull()){
                longitude = gsonObj.get("longitud").getAsString();
            }

            String address = "";
            if(!gsonObj.get("urlUbicacion").isJsonNull()){
                address = gsonObj.get("urlUbicacion").getAsString();
            }

            String url = "";
            if(!gsonObj.get("direccion").isJsonNull()){
                url = gsonObj.get("direccion").getAsString();
            }

            body.add(new InvitationSchedulerRequest(date,timeStart,timeEnd,room,address,latitude,longitude,url,dateFormat));
        }
        return new InvitationSchedulerListRequest(status,message,body);
    }
}
