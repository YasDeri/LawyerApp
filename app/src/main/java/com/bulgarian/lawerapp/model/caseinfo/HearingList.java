package com.bulgarian.lawerapp.model.caseinfo;

public class HearingList {

    private String id;
    private String user_id;
    private String case_id;
    private String name;
    private String client_id;
    private String mobile;
    private String email;
    private String place;
    private String latitude;
    private String longitude;
    private String notes;
    private String msg_court;
    private String event_date;
    private String event_time;
    private String remind_date;
    private String remind_time;
    private String strtotime;
    private String type;
    private String status;
    private String show_on_front;
    private String strtime;


    public HearingList(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCase_id() {
        return case_id;
    }

    public void setCase_id(String case_id) {
        this.case_id = case_id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUser_id() {
        return user_id;
    }


    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStrtotime() {
        return strtotime;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }

    public String getEvent_date() {
        return event_date;
    }

    public void setStrtotime(String strtotime) {
        this.strtotime = strtotime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEvent_time() {
        return event_time;
    }

    public void setEvent_time(String event_time) {
        this.event_time = event_time;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getMsg_court() {
        return msg_court;
    }

    public void setMsg_court(String msg_court) {
        this.msg_court = msg_court;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getRemind_date() {
        return remind_date;
    }

    public void setRemind_date(String remind_date) {
        this.remind_date = remind_date;
    }

    public String getRemind_time() {
        return remind_time;
    }

    public void setRemind_time(String remind_time) {
        this.remind_time = remind_time;
    }

    public String getShow_on_front() {
        return show_on_front;
    }

    public void setShow_on_front(String show_on_front) {
        this.show_on_front = show_on_front;
    }

    public String getStrtime() {
        return strtime;
    }

    public void setStrtime(String strtime) {
        this.strtime = strtime;
    }

}
