package com.guestlogix.travelercorekit.models;

public abstract class Answer {
    protected String questionId;
    protected String codedValue;

    public String getQuestionId() {
        return questionId;
    }

    public abstract String getCodedValue();

    public class AnswerError extends Error {

    }
}
