package com.qubicgo.android.appformacion.data.decoder;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.qubicgo.android.appformacion.data.request.CheckInListRequest;
import com.qubicgo.android.appformacion.data.request.CheckInRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CheckInListDeserializer implements JsonDeserializer<CheckInListRequest> {


    @Override
    public CheckInListRequest deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        ArrayList<CheckInRequest> body = new ArrayList<>();

        // Obtain Element
        String  status = (json).getAsJsonObject().get("status").getAsString();
        String  message = "";

        if ( (json).getAsJsonObject().get("message")!=null &&
                !(json).getAsJsonObject().get("message").isJsonNull()) {
            message=(json).getAsJsonObject().get("message").getAsString();
        }

        // Obtain Array
        JsonArray arrayList  = (json).getAsJsonObject().getAsJsonObject("body").getAsJsonArray("asistencias");


        // for each element of array
        for (JsonElement obj : arrayList) {

            // Object of array
            JsonObject gsonObj = obj.getAsJsonObject();

            // Primitives elements of object
            String type  = gsonObj.get("indicador").getAsString();
            String name = gsonObj.get("nombre").getAsString();

            Integer grpdId = gsonObj.get("grpdId").getAsInt();
            Integer roomId = gsonObj.get("salaId").getAsInt();
            String address = gsonObj.get("direccion").getAsString();

            String code = "";
            if(!gsonObj.get("codigo").isJsonNull()){
                code = gsonObj.get("codigo").getAsString();
            };

            body.add(new CheckInRequest(type,grpdId,roomId,name,address,code));
        }

        return new CheckInListRequest(status,message,body);
    }
}
