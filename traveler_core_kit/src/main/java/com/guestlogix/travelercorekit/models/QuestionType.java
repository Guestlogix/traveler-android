package com.guestlogix.travelercorekit.models;

import java.io.Serializable;
import java.util.List;

public abstract class QuestionType implements Serializable {
    // This class is setup this way to be easily transportable to Kotlin

    public enum Code {
        TEXTUAL, QUANTIYY, MULTIPLE_CHOICE, DATE
    }

    private Code code;

    QuestionType(Code code) {
        this.code = code;
    }

    public Code getCode() {
        return code;
    }

    // Types

    public static class Textual extends QuestionType {
        Textual() {
            super(Code.TEXTUAL);
        }
    }

    public static class Quantity extends QuestionType {
        Quantity() {
            super(Code.QUANTIYY);
        }
    }

    public static class MultipleChoice extends QuestionType {
        private List<Choice> choices;

        MultipleChoice(List<Choice> choices) {
            super(Code.MULTIPLE_CHOICE);
            this.choices = choices;
        }

        public List<Choice> getChoices() {
            return choices;
        }
    }

    public static class Date extends QuestionType {
        Date() {
            super(Code.DATE);
        }
    }
}
