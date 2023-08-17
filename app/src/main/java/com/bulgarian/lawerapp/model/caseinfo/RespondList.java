package com.bulgarian.lawerapp.model.caseinfo;

public class RespondList {

    private String id,case_id, name, mobile, lawyer, lawyer_mobile, lawyer_email;

    public RespondList(String id, String case_id, String name, String mobile,
                       String lawyer, String lawyer_mobile, String lawyer_email) {
        this.id = id;
        this.case_id = case_id;
        this.name = name;
        this.mobile = mobile;
        this.lawyer = lawyer;
        this.lawyer_mobile = lawyer_mobile;
        this.lawyer_email = lawyer_email;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public String getLawyer() {
        return lawyer;
    }

    public void setLawyer(String lawyer) {
        this.lawyer = lawyer;
    }

    public String getLawyer_email() {
        return lawyer_email;
    }

    public void setLawyer_email(String lawyer_email) {
        this.lawyer_email = lawyer_email;
    }

    public String getLawyer_mobile() {
        return lawyer_mobile;
    }

    public void setLawyer_mobile(String lawyer_mobile) {
        this.lawyer_mobile = lawyer_mobile;
    }
}
