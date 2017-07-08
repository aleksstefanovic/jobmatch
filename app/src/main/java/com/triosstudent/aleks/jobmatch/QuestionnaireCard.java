package com.triosstudent.aleks.jobmatch;

public class QuestionnaireCard {
    String question1, question2, question3, question4, title;

    public QuestionnaireCard (String q1, String q2, String q3, String q4, String t) {
        this.question1 = q1;
        this.question2 = q1;
        this.question3 = q3;
        this.question4 = q4;
        this.title = t;
    }

    public String getQuestion1 () {
        return this.question1;
    }

    public String getQuestion2 () {
        return this.question2;
    }

    public String getQuestion3 () {
        return this.question3;
    }

    public String getQuestion4 () {
        return this.question4;
    }

    public String getTitle () {
        return this.title;
    }
}
