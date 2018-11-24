package com.qubicgo.android.appformacion.data.decoder;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.qubicgo.android.appformacion.data.request.PollActiveRequest;
import com.qubicgo.android.appformacion.data.request.PollAnswerSettingRequest;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

public class PollAnswerSettingDeserializer  implements JsonDeserializer<PollAnswerSettingRequest> {

    @Override
    public PollAnswerSettingRequest deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        PollAnswerSettingRequest result = new PollAnswerSettingRequest();

        // Obtain Array
        Set<Map.Entry<String, JsonElement>> arrayList =  (json).getAsJsonObject().getAsJsonObject("body").getAsJsonObject().entrySet();
        for(Map.Entry<String, JsonElement> entry : arrayList){

            JsonArray elements=entry.getValue().getAsJsonArray();

            for(JsonElement element : elements){
                JsonObject obj = element.getAsJsonObject();

                Integer id = obj.get("id").getAsInt();
                String code = obj.get("clave").getAsString();
                String value = obj.get("valor").getAsString();

                result.setAnswerTypeByKey(entry.getKey(), new PollActiveRequest.PollAspect.PollQuestion.PollAnswer(id,code,value));
            }
        }

        return result;
    }
}
