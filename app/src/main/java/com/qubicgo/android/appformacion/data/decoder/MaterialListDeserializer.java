package com.qubicgo.android.appformacion.data.decoder;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.qubicgo.android.appformacion.data.request.MaterialListRequest;
import com.qubicgo.android.appformacion.data.request.MaterialRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MaterialListDeserializer implements JsonDeserializer<MaterialListRequest> {
    @Override
    public MaterialListRequest deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        ArrayList<MaterialRequest> body = new ArrayList<>();

        // Obtain Element
        String  status = (json).getAsJsonObject().get("status").getAsString();
        String  message = "";

        if ( (json).getAsJsonObject().get("message")!=null &&
                !(json).getAsJsonObject().get("message").isJsonNull()) {
            message=(json).getAsJsonObject().get("message").getAsString();
        }

        // Obtain Array
        JsonArray arrayList  = (json).getAsJsonObject().getAsJsonObject("body").getAsJsonArray("data");


        // for each element of array
        for (JsonElement obj : arrayList) {

            // Object of array
            JsonObject gsonObj = obj.getAsJsonObject();

            // Primitives elements of object
            String type  = gsonObj.get("indicador").getAsString();
            String name = gsonObj.get("nombre").getAsString();

            String urlDrive = "";
            if(!gsonObj.get("urlDrive").isJsonNull()){
                urlDrive = gsonObj.get("urlDrive").getAsString();
            };

            body.add(new MaterialRequest(name,type,urlDrive));
        }

        return new MaterialListRequest(status,message,body);
    }
}
