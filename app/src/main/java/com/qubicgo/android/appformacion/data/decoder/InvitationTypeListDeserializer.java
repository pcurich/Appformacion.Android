package com.qubicgo.android.appformacion.data.decoder;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.qubicgo.android.appformacion.data.request.InvitationTypeListRequest;
import com.qubicgo.android.appformacion.data.request.InvitationTypeRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class InvitationTypeListDeserializer  implements JsonDeserializer<InvitationTypeListRequest> {
    @Override
    public InvitationTypeListRequest deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        List<InvitationTypeRequest> body = new ArrayList<>();

        // Obtain Element
        String  status = (json).getAsJsonObject().get("status").getAsString();
        String  message = "";

        if ( (json).getAsJsonObject().get("message")!=null &&
                !(json).getAsJsonObject().get("message").isJsonNull()) {
            message=(json).getAsJsonObject().get("message").getAsString();
        }

        // Obtain Array
        JsonArray arrayList  = (json).getAsJsonObject().getAsJsonObject("body").getAsJsonArray("invitacionTipo");


        // for each element of array
        for (JsonElement obj : arrayList) {

            // Object of array
            JsonObject gsonObj = obj.getAsJsonObject();

            // Primitives elements of object
            Integer flag =  gsonObj.get("flag").getAsInt();
            String label  = gsonObj.get("label").getAsString();
            String code = gsonObj.get("tipoRespuesta").getAsString();

            body.add(new InvitationTypeRequest(flag,label,code));
        }

        return new InvitationTypeListRequest(status,message,body);
    }
}




