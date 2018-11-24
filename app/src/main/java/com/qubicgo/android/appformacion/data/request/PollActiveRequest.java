package com.qubicgo.android.appformacion.data.request;


import java.io.Serializable;
import java.util.List;

public class PollActiveRequest implements Serializable {

    private String type ;
    private String name;
    private Integer groupPersonId;
    private List<PollAspect> aspects;

    public PollActiveRequest(String type, String name, Integer groupPersonId, List<PollAspect> aspects) {
        this.type = type;
        this.name = name;
        this.groupPersonId = groupPersonId;
        this.aspects = aspects;
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

    public List<PollAspect> getAspects() {
        return aspects;
    }

    public void setAspects(List<PollAspect> aspects) {
        this.aspects = aspects;
    }

    public static class PollAspect  implements Serializable {

        private String description;
        private Integer teacherId;
        private String  teacherName;
        private Integer aspectId;
        private String  resposeType;
        private Integer resposeTypeId;
        private Integer scheduleId;
        private List<PollQuestion> questions;

        public PollAspect(String description, Integer teacherId, String teacherName, Integer aspectId, String resposeType, Integer resposeTypeId, Integer scheduleId, List<PollQuestion> questions) {
            this.description = description;
            this.teacherId = teacherId;
            this.teacherName = teacherName;
            this.aspectId = aspectId;
            this.resposeType = resposeType;
            this.resposeTypeId = resposeTypeId;
            this.scheduleId = scheduleId;
            this.questions = questions;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Integer getTeacherId() {
            return teacherId;
        }

        public void setTeacherId(Integer teacherId) {
            this.teacherId = teacherId;
        }

        public String getTeacherName() {
            return teacherName;
        }

        public void setTeacherName(String teacherName) {
            this.teacherName = teacherName;
        }

        public Integer getAspectId() {
            return aspectId;
        }

        public void setAspectId(Integer aspectId) {
            this.aspectId = aspectId;
        }

        public String getResposeType() {
            return resposeType;
        }

        public void setResposeType(String resposeType) {
            this.resposeType = resposeType;
        }

        public Integer getResposeTypeId() {
            return resposeTypeId;
        }

        public void setResposeTypeId(Integer resposeTypeId) {
            this.resposeTypeId = resposeTypeId;
        }

        public Integer getScheduleId() {
            return scheduleId;
        }

        public void setScheduleId(Integer scheduleId) {
            this.scheduleId = scheduleId;
        }

        public List<PollQuestion> getQuestions() {
            return questions;
        }

        public void setQuestions(List<PollQuestion> questions) {
            this.questions = questions;
        }

        public static class PollQuestion  implements Serializable{

            private Integer questionId;
            private String note;
            private String question;
            private List<PollAnswer> responseList ;

            public PollQuestion(Integer questionId, String note, String question, List<PollAnswer> responseList) {
                this.questionId = questionId;
                this.note = note;
                this.question = question;
                this.responseList = responseList;
            }

            public Integer getQuestionId() {
                return questionId;
            }

            public void setQuestionId(Integer questionId) {
                this.questionId = questionId;
            }

            public String getNote() {
                return note;
            }

            public void setNote(String note) {
                this.note = note;
            }

            public String getQuestion() {
                return question;
            }

            public void setQuestion(String question) {
                this.question = question;
            }

            public List<PollAnswer> getResponseList() {
                return responseList;
            }

            public void setResponseList(List<PollAnswer> responseList) {
                this.responseList = responseList;
            }

            public static class PollAnswer  implements Serializable {

                private Integer questionId;
                private Integer responseId;
                private String name;
                private String code;
                private String value;
                private boolean  isSelected;

                public PollAnswer(Integer questionId, Integer responseId, String name, String code, String value, boolean isSelected) {
                    this.questionId = questionId;
                    this.responseId = responseId;
                    this.name = name;
                    this.code = code;
                    this.value = value;
                    this.isSelected = isSelected;
                }

                public PollAnswer(Integer responseId, String code, String value) {
                    this.responseId = responseId;
                    this.code = code;
                    this.value = value;
                }


                public Integer getQuestionId() {
                    return questionId;
                }

                public void setQuestionId(Integer questionId) {
                    this.questionId = questionId;
                }

                public Integer getResponseId() {
                    return responseId;
                }

                public void setResponseId(Integer responseId) {
                    this.responseId = responseId;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getCode() {
                    return code;
                }

                public void setCode(String code) {
                    this.code = code;
                }

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }

                public boolean isSelected() {
                    return isSelected;
                }

                public void setSelected(boolean selected) {
                    isSelected = selected;
                }
            }
        }
    }
}
