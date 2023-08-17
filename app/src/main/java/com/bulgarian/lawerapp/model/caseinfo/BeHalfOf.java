package com.bulgarian.lawerapp.model.caseinfo;

public class BeHalfOf {

    private String id,behalf;


    public BeHalfOf(String id, String behalf) {
        this.id = id;
        this.behalf = behalf;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBehalf() {
        return behalf;
    }

    public void setBehalf(String behalf) {
        this.behalf = behalf;
    }
}
