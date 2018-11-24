package com.qubicgo.android.appformacion.data.decoder;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.qubicgo.android.appformacion.data.request.SchedulerListRequest;
import com.qubicgo.android.appformacion.data.request.SchedulerRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SchedulerListDeserializer implements JsonDeserializer<SchedulerListRequest> {

    @Override
    public SchedulerListRequest deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        ArrayList<SchedulerRequest> body = new ArrayList<>();

        // Obtain Element
        String  status = (json).getAsJsonObject().get("status").getAsString();
        String  message = "";

        if ( (json).getAsJsonObject().get("message")!=null &&
                !(json).getAsJsonObject().get("message").isJsonNull()) {
            message=(json).getAsJsonObject().get("message").getAsString();
        }

        // Obtain Array
        JsonArray arrayList  = (json).getAsJsonObject().getAsJsonObject("body").getAsJsonArray("actividadesProgramadas");


        // for each element of array
        for (JsonElement obj : arrayList) {

            // Object of array
            JsonObject gsonObj = obj.getAsJsonObject();

            // Primitives elements of object
            String type  = gsonObj.get("indicador").getAsString();
            String target = gsonObj.get("objetivo").getAsString();
            String name = gsonObj.get("nombre").getAsString();
            String sessions = gsonObj.get("sesiones").getAsString();
            Integer groupPersonId =  gsonObj.get("grupoPersonaId").getAsInt();
            String hashTag = "";
            if(!gsonObj.get("hashTag").isJsonNull()){
                hashTag = gsonObj.get("hashTag").getAsString();
            };
            String urlDrive = "";
            if(!gsonObj.get("urlDrive").isJsonNull()){
                urlDrive = gsonObj.get("urlDrive").getAsString();
            };

            String pdfUrl = "";
            if(!gsonObj.get("pdfurl").isJsonNull()){
                pdfUrl = gsonObj.get("pdfurl").getAsString();
            }

            body.add(new SchedulerRequest(type,target,name,sessions,urlDrive,pdfUrl,groupPersonId,hashTag));
        }

        return new SchedulerListRequest(status,message,body);
    }
}
