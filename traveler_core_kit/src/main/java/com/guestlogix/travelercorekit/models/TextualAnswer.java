package com.guestlogix.travelercorekit.models;

public class TextualAnswer extends Answer {
    private String value;

    public TextualAnswer(String value, Question question) {
        if (!(question.getType() instanceof QuestionType.Textual)) {
            throw new IllegalArgumentException("Question must be of Textual type");
        }

        this.value = value;
        this.questionId = question.getId();
    }

    @Override
    public String getCodedValue() {
        return value;
    }

    public String getValue() {
        return value;
    }
}
