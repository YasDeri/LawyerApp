package com.bulgarian.lawerapp.model;

public class DialogData {
    public String sub_title_1, sub_title_2, sub_title_3;

    public DialogData(String sub_title_1, String sub_title_2, String sub_title_3) {
        this.sub_title_1 = sub_title_1;
        this.sub_title_2 = sub_title_2;
        this.sub_title_3 = sub_title_3;

    }

    public String getSub_title_1() {
        return sub_title_1;
    }

    public String getSub_title_2() {
        return sub_title_2;
    }

    public String getSub_title_3() {
        return sub_title_3;
    }
}
