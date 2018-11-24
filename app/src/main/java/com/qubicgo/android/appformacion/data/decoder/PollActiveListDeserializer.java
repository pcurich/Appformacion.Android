package com.qubicgo.android.appformacion.data.decoder;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.qubicgo.android.appformacion.data.request.PollActiveListRequest;
import com.qubicgo.android.appformacion.data.request.PollActiveRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PollActiveListDeserializer implements JsonDeserializer<PollActiveListRequest> {
    @Override
    public PollActiveListRequest deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        ArrayList<PollActiveRequest> body = new ArrayList<>();

        // Obtain Element
        String  status = (json).getAsJsonObject().get("status").getAsString();
        String  message = "";

        if ( (json).getAsJsonObject().get("message")!=null &&
                !(json).getAsJsonObject().get("message").isJsonNull()) {
            message=(json).getAsJsonObject().get("message").getAsString();
        }

        // Obtain Array
        JsonArray arraysPollList  = (json).getAsJsonObject().get("body").getAsJsonArray();

        for (JsonElement objAspc : arraysPollList) {

            String type =             objAspc.getAsJsonObject().get("indicador").getAsString();
            String name =             objAspc.getAsJsonObject().get("nombre").getAsString();
            Integer grupoPersonaId  = objAspc.getAsJsonObject().get("grupoPersonaId").getAsInt();
            // Obtain Array
            JsonArray arrayAspectList  = objAspc.getAsJsonObject().getAsJsonArray("encuestas");
            List<PollActiveRequest.PollAspect> aspects = new ArrayList<>();

            // for each element of array
            for (JsonElement obj : arrayAspectList) {

                // Object of array
                JsonObject gsonObj = obj.getAsJsonObject();

                String descripcion  = gsonObj.get("aspectoDetalle").getAsString();

                Integer teacherId = 0;
                if(!gsonObj.get("expositorId").isJsonNull()){
                    teacherId = gsonObj.get("expositorId").getAsInt();
                }

                String teacherName = "";
                if(!gsonObj.get("expositorNombre").isJsonNull()){
                    teacherName = gsonObj.get("expositorNombre").getAsString();
                }

                Integer aspectId = gsonObj.get("encuestaId").getAsInt();
                String resposeType = gsonObj.get("respuestaCode").getAsString();

                Integer resposeTypeId = gsonObj.get("respuestaId").getAsInt();
                Integer scheduleId = gsonObj.get("scheduleId").getAsInt();
                JsonArray arrayListQuestions = gsonObj.getAsJsonArray("preguntas");

                List<PollActiveRequest.PollAspect.PollQuestion> listQuestion = new ArrayList<>();
                for (JsonElement q : arrayListQuestions) {
                    // Object of array
                    JsonObject gsonObj2 = q.getAsJsonObject();

                    Integer  questionId  = gsonObj2.get("id").getAsInt();

                    String note = "";
                    if(!gsonObj2.get("nota").isJsonNull()){
                        note = gsonObj2.get("nota").getAsString();
                    }

                    String question  = gsonObj2.get("pregunta").getAsString();

                    listQuestion.add( new PollActiveRequest.PollAspect.PollQuestion(questionId,note,question,null));
                }
                aspects.add(new PollActiveRequest.PollAspect(
                        descripcion,teacherId,teacherName,aspectId,resposeType,resposeTypeId,scheduleId,listQuestion));

            }
            body.add(new PollActiveRequest(type,name,grupoPersonaId,aspects));
        }

        return new PollActiveListRequest(status,message,body);
    }
}
