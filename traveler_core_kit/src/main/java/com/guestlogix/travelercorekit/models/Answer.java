package com.guestlogix.travelercorekit.models;

import java.io.Serializable;

public abstract class Answer implements Serializable {
    protected String questionId;
    protected String codedValue;

    public String getQuestionId() {
        return questionId;
    }

    public abstract String getCodedValue();

    public class AnswerError extends Error {

    }
}
