package com.qubicgo.android.appformacion.data.decoder;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.qubicgo.android.appformacion.data.request.EvaluationActiveListRequest;
import com.qubicgo.android.appformacion.data.request.EvaluationActiveRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EvaluationActiveListDeserializer implements JsonDeserializer<EvaluationActiveListRequest> {


    @Override
    public EvaluationActiveListRequest deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        ArrayList<EvaluationActiveRequest> body = new ArrayList<>();

        // Obtain Element
        String  status = (json).getAsJsonObject().get("status").getAsString();
        String  message = "";

        if ( (json).getAsJsonObject().get("message")!=null &&
                !(json).getAsJsonObject().get("message").isJsonNull()) {
            message=(json).getAsJsonObject().get("message").getAsString();
        }

        // Obtain Array
        JsonArray arraysEvaluationList  = (json).getAsJsonObject().get("body").getAsJsonArray();

        for (JsonElement objAspc : arraysEvaluationList) {

            String type =             objAspc.getAsJsonObject().get("indicador").getAsString();
            String name =             objAspc.getAsJsonObject().get("nombre").getAsString();
            String  groupId =         objAspc.getAsJsonObject().get("grupoId").getAsString();
            Integer groupPersoId =    objAspc.getAsJsonObject().get("grupoPersonaId").getAsInt();

            // Obtain Array
            JsonArray arrayAspectList  = objAspc.getAsJsonObject().getAsJsonArray("evaluacionesList");
            List<EvaluationActiveRequest.EvaluationRequest> aspects = new ArrayList<>();

            // for each element of array
            for (JsonElement obj : arrayAspectList) {

                // Object of array
                JsonObject gsonObj = obj.getAsJsonObject();

                Integer  percentage   = gsonObj.get("porcentaje").getAsInt();
                Integer grppId        = gsonObj.get("grppId").getAsInt();
                String aspect         = gsonObj.get("evaluacionNombre").getAsString();

                JsonArray arrayListQuestions = gsonObj.getAsJsonArray("preguntas");

                List<EvaluationActiveRequest.EvaluationRequest.QuestionRequest> listQuestion = new ArrayList<>();
                for (JsonElement q : arrayListQuestions) {
                    // Object of array
                    JsonObject gsonObj2 = q.getAsJsonObject();

                    Integer  questionId  = gsonObj2.get("preguntaId").getAsInt();
                    Integer  orden  = gsonObj2.get("nroOrden").getAsInt();
                    String question  = gsonObj2.get("pregunta").getAsString();
                    String codeQuestion  = gsonObj2.get("codPregunta").getAsString();

                    JsonArray arrayListAlternative = gsonObj2.getAsJsonArray("alternativas");

                    List<EvaluationActiveRequest.EvaluationRequest.QuestionRequest.AlternativeRequest> alternatives = new ArrayList<>();
                    for (JsonElement q1 : arrayListAlternative) {

                        JsonObject gsonObj3 = q1.getAsJsonObject();

                        Integer  alternativeId  = gsonObj3.get("alternativaId").getAsInt();
                        String alternative  = gsonObj3.get("descripcion").getAsString();
                        String codeAlternative  = gsonObj3.get("codRespuesta").getAsString();
                        alternatives.add(new EvaluationActiveRequest.EvaluationRequest.QuestionRequest.AlternativeRequest(alternativeId,alternative,codeAlternative));

                    }

                    listQuestion.add( new EvaluationActiveRequest.EvaluationRequest.QuestionRequest(questionId,orden,question,codeQuestion,alternatives));
                }
                aspects.add(new EvaluationActiveRequest.EvaluationRequest(percentage,aspect,grppId,listQuestion));
            }
            body.add(new EvaluationActiveRequest(groupId,type,name,groupPersoId,aspects));
        }

        return new EvaluationActiveListRequest(status,message,body);
    }
}
