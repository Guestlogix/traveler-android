package com.guestlogix.travelercorekit.models;

import java.io.Serializable;

public abstract class Answer implements Serializable {
    String questionId;
    String codedValue;

    public abstract String getCodedValue();

    public String getQuestionId() {
        return questionId;
    }

    class AnswerError extends Error {

    }
}
