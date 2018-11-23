package com.qubicgo.android.appformacion.data.decoder;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.qubicgo.android.appformacion.data.request.CheckIn;

import java.lang.reflect.Type;

public class CheckInDeserializer   implements JsonDeserializer<CheckIn> {
    @Override
    public CheckIn deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String  status = (json).getAsJsonObject().get("status").getAsString();
        String  message = "";

        if ( (json).getAsJsonObject().get("message")!=null &&
                !(json).getAsJsonObject().get("message").isJsonNull()) {
            message=(json).getAsJsonObject().get("message").getAsString();
        }

        return new CheckIn(message,status);
    }
}
