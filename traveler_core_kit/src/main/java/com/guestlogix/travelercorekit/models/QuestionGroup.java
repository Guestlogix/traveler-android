package com.guestlogix.travelercorekit.models;

import java.io.Serializable;
import java.util.List;

public class QuestionGroup implements Serializable {
    private String title;
    private String disclaimer;
    private List<Question> questions;

    public QuestionGroup(String title, String disclaimer, List<Question> questions) {
        this.title = title;
        this.disclaimer = disclaimer;
        this.questions = questions;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public String getTitle() {
        return title;
    }

    public String getDisclaimer() {
        return disclaimer;
    }
}
