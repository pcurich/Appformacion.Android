package com.qubicgo.android.appformacion.data.decoder;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.qubicgo.android.appformacion.data.request.InvitationListRequest;
import com.qubicgo.android.appformacion.data.request.InvitationRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class InvitationListDeserializer implements JsonDeserializer<InvitationListRequest> {


    @Override
    public InvitationListRequest deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        ArrayList<InvitationRequest> body = new ArrayList<>();

        // Obtain Element
        String  status = (json).getAsJsonObject().get("status").getAsString();
        String  message = "";

        if ( (json).getAsJsonObject().get("message")!=null &&
            !(json).getAsJsonObject().get("message").isJsonNull()) {
            message=(json).getAsJsonObject().get("message").getAsString();
        }

        // Obtain Array
        JsonArray arrayList  = (json).getAsJsonObject().getAsJsonObject("body").getAsJsonArray("cursosPendientesAceptacion");


        // for each element of array
        for (JsonElement obj : arrayList) {

            // Object of array
            JsonObject gsonObj = obj.getAsJsonObject();

            // Primitives elements of object
            String type  = gsonObj.get("indicador").getAsString();
            String hours = gsonObj.get("horas").getAsString();
            String minutes = gsonObj.get("minutos").getAsString();
            String name = gsonObj.get("nombre").getAsString();
            String target = gsonObj.get("objetivo").getAsString();
            String scheduleId = gsonObj.get("horarioId").getAsString();
            String groupId =  gsonObj.get("grupoId").getAsString();
            String date = gsonObj.get("fecha").getAsString();
            String nSessions = gsonObj.get("nroSessiones").getAsString();

            String urlSilabus = "";
            if(!gsonObj.get("urlSilabus").isJsonNull()){
                urlSilabus = gsonObj.get("urlSilabus").getAsString();
            };

            String urlDrive = "";
            if(!gsonObj.get("urlDrive").isJsonNull()){
                urlDrive = gsonObj.get("urlDrive").getAsString();
            }

            body.add(new InvitationRequest(type,hours,minutes,name,target,scheduleId,groupId,urlSilabus,urlDrive,date,nSessions));
        }

        return new InvitationListRequest(status,message,body);
    }
}
