package com.triosstudent.aleks.jobmatch.responses;

public class ResponseCard {
    String response1, response2, response3, response4;
    int user_id, id;

    public ResponseCard(String r1, String r2, String r3, String r4, int ui, int i) {
        this.response1 = r1;
        this.response2 = r2;
        this.response3 = r3;
        this.response4 = r4;
        this.user_id = ui;
        this.id = i;
    }

    public String getResponse1 () {
        return this.response1;
    }

    public String getResponse2 () {
        return this.response2;
    }

    public String getResponse3 () {
        return this.response3;
    }

    public String getResponse4 () {
        return this.response4;
    }

    public int getId () {
        return this.id;
    }

    public int getUserId () {
        return this.user_id;
    }
}
