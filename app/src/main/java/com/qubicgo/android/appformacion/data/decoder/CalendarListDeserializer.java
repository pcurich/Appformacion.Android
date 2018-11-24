package com.qubicgo.android.appformacion.data.decoder;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.qubicgo.android.appformacion.data.request.CalendarListRequest;
import com.qubicgo.android.appformacion.data.request.CalendarRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CalendarListDeserializer implements JsonDeserializer<CalendarListRequest> {

    @Override
    public CalendarListRequest deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        ArrayList<CalendarRequest> body = new ArrayList<>();

        // Obtain Element
        String status = (json).getAsJsonObject().get("status").getAsString();
        String message = "";

        if ((json).getAsJsonObject().get("message") != null &&
                !(json).getAsJsonObject().get("message").isJsonNull()) {
            message = (json).getAsJsonObject().get("message").getAsString();
        }

        // Obtain Array
        JsonArray arrayList = (json).getAsJsonObject().getAsJsonObject("body").getAsJsonArray("calendario");

        // for each element of array
        for (JsonElement obj : arrayList) {

            // Object of array
            JsonObject gsonObj = obj.getAsJsonObject();

            // Primitives elements of object
            String fechaKey = gsonObj.get("fechaKey").getAsString();
            JsonArray fechaValue = gsonObj.getAsJsonArray("fechaValue");

            List<CalendarRequest.CalendarDetailRequest> list = new ArrayList<>();

            for (JsonElement obj2 : fechaValue) {
                // Object of array
                JsonObject gsonObj2 = obj2.getAsJsonObject();

                String fecha = gsonObj2.get("fecha").getAsString();
                String nombre = gsonObj2.get("nombre").getAsString();
                String fin = gsonObj2.get("fin").getAsString();
                String inicio = gsonObj2.get("inicio").getAsString();
                String sala = gsonObj2.get("sala").getAsString();

                list.add(new CalendarRequest.CalendarDetailRequest(fecha,nombre,fin,inicio,sala));
            }

            body.add(new CalendarRequest(fechaKey,list));
        }

        return new CalendarListRequest(status,message,body);
    }
}
