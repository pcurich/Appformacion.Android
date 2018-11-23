package com.qubicgo.android.appformacion.data.response;

import java.io.Serializable;

public class PollActiveResponse implements Serializable {

    private Integer preguntaId;
    private Integer rtaId;
    private String  rtadDescription;

    public PollActiveResponse(Integer preguntaId, Integer rtaId, String rtadDescription) {
        this.preguntaId = preguntaId;
        this.rtaId = rtaId;
        this.rtadDescription = rtadDescription;
    }

    public Integer getPreguntaId() {
        return preguntaId;
    }

    public void setPreguntaId(Integer preguntaId) {
        this.preguntaId = preguntaId;
    }

    public Integer getRtaId() {
        return rtaId;
    }

    public void setRtaId(Integer rtaId) {
        this.rtaId = rtaId;
    }

    public String getRtadDescription() {
        return rtadDescription;
    }

    public void setRtadDescription(String rtadDescription) {
        this.rtadDescription = rtadDescription;
    }
}
