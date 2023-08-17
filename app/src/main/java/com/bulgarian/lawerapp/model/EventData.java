package com.bulgarian.lawerapp.model;

public class EventData {
    public String id, date, clientName, time, type,case_no,year;

    public EventData(String id, String date, String clientName, String time, String type) {
        this.id = id;
        this.clientName = clientName;
        this.time = time;
        this.date = date;
        this.type = type;

    }

    public EventData(String id, String event_date, String name, String event_time, String type, String case_no, String year){
        this.id = id;
        this.clientName = name;
        this.time = event_time;
        this.date = event_date;
        this.type = type;
        this.case_no=case_no;
        this.year=year;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCase_no() {
        return case_no;
    }

    public String getYear() {
        return year;
    }
}
