package com.qubicgo.android.appformacion.data.response;

import java.io.Serializable;
import java.util.List;

public class EvaluationActiveResponse implements Serializable {

    private Integer preguntaId;
    private String codPregunta;
    private List<Integer> respuestas;
    private Boolean esCorrecto;

    public EvaluationActiveResponse(Integer preguntaId, String codPregunta, List<Integer> respuestas, Boolean esCorrecto) {
        this.preguntaId = preguntaId;
        this.codPregunta = codPregunta;
        this.respuestas = respuestas;
        this.esCorrecto = esCorrecto;
    }

    public Integer getPreguntaId() {
        return preguntaId;
    }

    public void setPreguntaId(Integer preguntaId) {
        this.preguntaId = preguntaId;
    }

    public String getCodPregunta() {
        return codPregunta;
    }

    public void setCodPregunta(String codPregunta) {
        this.codPregunta = codPregunta;
    }

    public List<Integer> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(List<Integer> respuestas) {
        this.respuestas = respuestas;
    }

    public Boolean getEsCorrecto() {
        return esCorrecto;
    }

    public void setEsCorrecto(Boolean esCorrecto) {
        this.esCorrecto = esCorrecto;
    }
}
