package com.qubicgo.android.appformacion.data.request;

import java.util.List;

public class CalendarRequest {
    private String fechaKey;
    private List<CalendarDetailRequest> calendarDetailRequest;

    public CalendarRequest(String fechaKey, List<CalendarDetailRequest> calendarDetailRequest) {
        this.fechaKey = fechaKey;
        this.calendarDetailRequest = calendarDetailRequest;
    }

    public String getFechaKey() {
        return fechaKey;
    }

    public void setFechaKey(String fechaKey) {
        this.fechaKey = fechaKey;
    }

    public List<CalendarDetailRequest> getCalendarDetailRequest() {
        return calendarDetailRequest;
    }

    public void setCalendarDetailRequest(List<CalendarDetailRequest> calendarDetailRequest) {
        this.calendarDetailRequest = calendarDetailRequest;
    }

    public static class CalendarDetailRequest {
        private String date;
        private String name;
        private String end;
        private String start;
        private String room;

        public CalendarDetailRequest(String date, String name, String end, String start, String room) {
            this.date = date;
            this.name = name;
            this.end = end;
            this.start = start;
            this.room = room;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEnd() {
            return end;
        }

        public void setEnd(String end) {
            this.end = end;
        }

        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public String getRoom() {
            return room;
        }

        public void setRoom(String room) {
            this.room = room;
        }
    }
}
