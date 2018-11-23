package com.qubicgo.android.appformacion.data.decoder;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.qubicgo.android.appformacion.data.request.DashboardDetailRequest;
import com.qubicgo.android.appformacion.data.request.DashboardRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DashboardDeserializer  implements JsonDeserializer<DashboardRequest> {

    @Override
    public DashboardRequest deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        ArrayList<DashboardDetailRequest> details = new ArrayList<>();

        // Obtain Array
        JsonArray  arrayList  = (json).getAsJsonObject().getAsJsonObject("body").getAsJsonArray("dashboard");

        // for each element of array
        for (JsonElement obj :arrayList) {

            // Object of array
            JsonObject gsonObj = obj.getAsJsonObject();

            // Primitives elements of object
            String  key  = gsonObj.get("key").getAsString();
            String  line  = gsonObj.get("detalle").getAsString();
            String  nextStep  = gsonObj.get("goto").getAsString();
            int  count  = gsonObj.get("cantidad").getAsInt();

            details.add(new DashboardDetailRequest(key,line,count,nextStep));
        }

        return new DashboardRequest(details);

    }
}
