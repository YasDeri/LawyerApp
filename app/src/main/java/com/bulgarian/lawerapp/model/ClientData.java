package com.bulgarian.lawerapp.model;

public class ClientData {

    private String id,user_id,client_name,mobile,email,notes,strtottime;

    public ClientData(String id, String user_id, String client_name, String mobile, String email, String notes, String strtottime) {
        this.id = id;
        this.user_id = user_id;
        this.client_name = client_name;
        this.mobile = mobile;
        this.email = email;
        this.notes = notes;
        this.strtottime = strtottime;
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

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public String getStrtottime() {
        return strtottime;
    }

    public void setStrtottime(String strtottime) {
        this.strtottime = strtottime;
    }
}
