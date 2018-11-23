package com.qubicgo.android.appformacion.data.decoder;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.qubicgo.android.appformacion.data.response.EvaluationResponse;

import java.lang.reflect.Type;

public class EvaluationResponseDeserializer  implements JsonDeserializer<EvaluationResponse> {

    @Override
    public EvaluationResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String  message = "";
        if( (json).getAsJsonObject().get("message")!=null &&
                !(json).getAsJsonObject().get("message").isJsonNull()) {
            message =  (json).getAsJsonObject().get("message").getAsString();
        }

        String  status="";
        if( (json).getAsJsonObject().get("status")!=null &&
                !(json).getAsJsonObject().get("status").isJsonNull()) {
            status = (json).getAsJsonObject().get("status").getAsString();
        }

        boolean  isError = false;
        if( (json).getAsJsonObject().get("isError")!=null &&
                !(json).getAsJsonObject().get("isError").isJsonNull()) {
            isError = (json).getAsJsonObject().get("isError").getAsBoolean();
        }

        return new EvaluationResponse(message,status,isError);
    }
}
