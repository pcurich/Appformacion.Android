package com.qubicgo.android.appformacion.data.decoder;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.qubicgo.android.appformacion.data.request.CheckInDetailListRequest;
import com.qubicgo.android.appformacion.data.request.CheckInDetailRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CheckInDetailListDeserializer implements JsonDeserializer<CheckInDetailListRequest> {

    @Override
    public CheckInDetailListRequest deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        ArrayList<CheckInDetailRequest> body = new ArrayList<>();

        // Obtain Element
        String  status = (json).getAsJsonObject().get("status").getAsString();
        String  message = "";

        if ( (json).getAsJsonObject().get("message")!=null &&
                !(json).getAsJsonObject().get("message").isJsonNull()) {
            message=(json).getAsJsonObject().get("message").getAsString();
        }

        // Obtain Array
        JsonArray arrayList  = (json).getAsJsonObject().getAsJsonObject("body").getAsJsonArray("asistenciasDetalle");


        // for each element of array
        for (JsonElement obj : arrayList) {

            // Object of array
            JsonObject gsonObj = obj.getAsJsonObject();

            // Primitives elements of object
            String type  = gsonObj.get("indicador").getAsString();

            String course = "";
            if(!gsonObj.get("cursoNombre").isJsonNull()){
                course = gsonObj.get("cursoNombre").getAsString();;
            };

            String program = "";
            if(!gsonObj.get("programaNombre").isJsonNull()){
                program = gsonObj.get("programaNombre").getAsString();;
            };

            String date = gsonObj.get("fecha").getAsString();
            String startTime = gsonObj.get("inicio").getAsString();
            String endTime = gsonObj.get("final").getAsString();

            Integer schedulerId = gsonObj.get("horarioid").getAsInt();
            String addredss = gsonObj.get("direccion").getAsString();

            body.add(new CheckInDetailRequest(type,course,program,date,startTime,endTime,schedulerId,addredss));
        }

        return new CheckInDetailListRequest(status,message,body);
    }
}
