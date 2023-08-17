package com.bulgarian.lawerapp.model;

/**
 * ======> Created by dheeraj-gangwar on Sat, 2019-May-18 <======
 */
public class AssociateData {

    private String id,name,email,phone,assoc_username,assoc_password;


    public AssociateData(String id, String name, String email, String phone,String assoc_username,String assoc_password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.assoc_username = assoc_username;
        this.assoc_password = assoc_password;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getAssoc_username() {
        return assoc_username;
    }

    public void setAssoc_username(String assoc_username) {
        this.assoc_username = assoc_username;
    }

    public String getAssoc_password() {
        return assoc_password;
    }

    public void setAssoc_password(String assoc_password) {
        this.assoc_password = assoc_password;
    }
}
