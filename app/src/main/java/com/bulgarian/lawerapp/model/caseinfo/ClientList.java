package com.bulgarian.lawerapp.model.caseinfo;

public class ClientList {

    private String id,user_id, client_name, mobile, email, notes, strtotime;


    public ClientList(String id, String user_id, String client_name, String mobile,
                      String email, String notes, String strtotime) {
        this.id = id;
        this.user_id = user_id;
        this.client_name = client_name;
        this.mobile = mobile;
        this.email = email;
        this.notes = notes;
        this.strtotime = strtotime;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStrtotime() {
        return strtotime;
    }

    public void setStrtotime(String strtotime) {
        this.strtotime = strtotime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

}
