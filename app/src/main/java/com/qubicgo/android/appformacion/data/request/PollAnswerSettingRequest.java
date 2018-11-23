package com.qubicgo.android.appformacion.data.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PollAnswerSettingRequest {

    private Map<String,List<PollActiveRequest.PollAspect.PollQuestion.PollAnswer>> answerType ;

    public PollAnswerSettingRequest(Map<String, List<PollActiveRequest.PollAspect.PollQuestion.PollAnswer>> answerType) {
        this.answerType = answerType;
    }

    public PollAnswerSettingRequest() {
        answerType = new HashMap<>();
    }

    public List<PollActiveRequest.PollAspect.PollQuestion.PollAnswer> getAnswerTypeByKey(String key) {
        List<PollActiveRequest.PollAspect.PollQuestion.PollAnswer> result = new ArrayList<>();
        for (PollActiveRequest.PollAspect.PollQuestion.PollAnswer r : answerType.get(key)){
            result.add(new PollActiveRequest.PollAspect.PollQuestion.PollAnswer(0,r.getResponseId(),r.getName(),r.getCode(),r.getValue(),false));
        }
        return result;
    }

    public void setAnswerTypeByKey(String key, PollActiveRequest.PollAspect.PollQuestion.PollAnswer answer) {
        List<PollActiveRequest.PollAspect.PollQuestion.PollAnswer> answers = answerType.get(key);
        if(answers==null ||  answers.isEmpty()){
            answers= new ArrayList<>();
        }
        answers.add(answer);
        answerType.put(key,answers);
    }

}
