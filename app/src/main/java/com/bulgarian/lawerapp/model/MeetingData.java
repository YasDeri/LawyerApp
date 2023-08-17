package com.bulgarian.lawerapp.model;

public class MeetingData {

    private String id,user_id,name,client_id,email,mobile,place,latitude,longitude,notes,event_date,event_time,remind_date,remind_time,strtotime;


    public MeetingData(String id, String user_id, String name, String client_id, String email, String mobile, String place, String latitude, String longitude, String notes, String event_date, String event_time, String remind_date, String remind_time, String strtotime) {
        this.id = id;
        this.user_id = user_id;
        this.name = name;
        this.client_id = client_id;
        this.email = email;
        this.mobile = mobile;
        this.place = place;
        this.latitude = latitude;
        this.longitude = longitude;
        this.notes = notes;
        this.event_date = event_date;
        this.event_time = event_time;
        this.remind_date = remind_date;
        this.remind_time = remind_time;
        this.strtotime = strtotime;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getEvent_date() {
        return event_date;
    }

    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }

    public String getEvent_time() {
        return event_time;
    }

    public void setEvent_time(String event_time) {
        this.event_time = event_time;
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

    public String getStrtotime() {
        return strtotime;
    }

    public void setStrtotime(String strtotime) {
        this.strtotime = strtotime;
    }
}
