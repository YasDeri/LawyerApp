package com.bulgarian.lawerapp.model.caseinfo;

public class CaseType {

    private String id,kind;


    public CaseType(String id, String kind) {
        this.id = id;
        this.kind = kind;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }
}
