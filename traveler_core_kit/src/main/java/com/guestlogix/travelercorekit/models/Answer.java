package com.guestlogix.travelercorekit.models;

import java.io.Serializable;

public abstract class Answer implements Serializable {
    String questionId;
    String codedValue;

    public abstract String getCodedValue();

    class AnswerError extends Error {

    }
}
