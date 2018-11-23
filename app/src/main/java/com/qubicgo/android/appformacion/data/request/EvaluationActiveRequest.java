package com.qubicgo.android.appformacion.data.request;

import java.io.Serializable;
import java.util.List;

public class EvaluationActiveRequest implements Serializable {

    private String groupiId;
    private String type;
    private String name;
    private Integer groupPersonId;
    private List<EvaluationRequest> evaluations;

    public EvaluationActiveRequest(String groupiId, String type, String name, Integer groupPersonId, List<EvaluationRequest> evaluations) {
        this.groupiId = groupiId;
        this.type = type;
        this.name = name;
        this.groupPersonId = groupPersonId;
        this.evaluations = evaluations;
    }

    public String getGroupiId() {
        return groupiId;
    }

    public void setGroupiId(String groupiId) {
        this.groupiId = groupiId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGroupPersonId() {
        return groupPersonId;
    }

    public void setGroupPersonId(Integer groupPersonId) {
        this.groupPersonId = groupPersonId;
    }

    public List<EvaluationRequest> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(List<EvaluationRequest> evaluations) {
        this.evaluations = evaluations;
    }

    public static class EvaluationRequest implements Serializable {
        private Integer percentage;
        private String name;
        private Integer grppId;
        private List<QuestionRequest> questions;

        public EvaluationRequest(Integer percentage, String name, Integer grppId, List<QuestionRequest> questions) {
            this.percentage = percentage;
            this.name = name;
            this.grppId = grppId;
            this.questions = questions;
        }

        public Integer getPercentage() {
            return percentage;
        }

        public void setPercentage(Integer percentage) {
            this.percentage = percentage;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getGrppId() {
            return grppId;
        }

        public void setGrppId(Integer grppId) {
            this.grppId = grppId;
        }

        public List<QuestionRequest> getQuestions() {
            return questions;
        }

        public void setQuestions(List<QuestionRequest> questions) {
            this.questions = questions;
        }

        public static class QuestionRequest implements Serializable {
            private Integer questionId;
            private Integer orden;
            private String question;
            private String codeQuestion;
            private List<AlternativeRequest> alternatives;

            public QuestionRequest(Integer questionId, Integer orden, String question, String codeQuestion, List<AlternativeRequest> alternatives) {
                this.questionId = questionId;
                this.orden = orden;
                this.question = question;
                this.codeQuestion = codeQuestion;
                this.alternatives = alternatives;
            }

            public Integer getQuestionId() {
                return questionId;
            }

            public void setQuestionId(Integer questionId) {
                this.questionId = questionId;
            }

            public Integer getOrden() {
                return orden;
            }

            public void setOrden(Integer orden) {
                this.orden = orden;
            }

            public String getQuestion() {
                return question;
            }

            public void setQuestion(String question) {
                this.question = question;
            }

            public String getCodeQuestion() {
                return codeQuestion;
            }

            public void setCodeQuestion(String codeQuestion) {
                this.codeQuestion = codeQuestion;
            }

            public List<AlternativeRequest> getAlternatives() {
                return alternatives;
            }

            public void setAlternatives(List<AlternativeRequest> alternatives) {
                this.alternatives = alternatives;
            }

            public static class AlternativeRequest implements Serializable {
                private Integer alternativeId;
                private String description;
                private String CodeAlternative;
                private boolean isSelected;

                public AlternativeRequest(Integer alternativeId, String description, String codeAlternative) {
                    this.alternativeId = alternativeId;
                    this.description = description;
                    CodeAlternative = codeAlternative;
                }

                public boolean isSelected() {
                    return isSelected;
                }

                public void setSelected(boolean selected) {
                    isSelected = selected;
                }

                public Integer getAlternativeId() {
                    return alternativeId;
                }

                public void setAlternativeId(Integer alternativeId) {
                    this.alternativeId = alternativeId;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public String getCodeAlternative() {
                    return CodeAlternative;
                }

                public void setCodeAlternative(String codeAlternative) {
                    CodeAlternative = codeAlternative;
                }
            }
        }
    }
}
