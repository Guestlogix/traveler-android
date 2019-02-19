package com.guestlogix.travelercorekit.models;

public class MultipleChoiceSelection extends Answer {
    private int value;

    public MultipleChoiceSelection(int value, Question question) {
        if (!(question.getType() instanceof MultipleChoiceType)) {
            // Throw exception;
            throw new AnswerError();
        }

        MultipleChoiceType type = (MultipleChoiceType) question.getType();

        if (value < 0 || value >= type.getChoices().size()) {
            throw new AnswerError();
        }

        this.value = value;
        this.codedValue = type.getChoices().get(value).getId();
        this.questionId = question.getId();
    }

    @Override
    public String getCodedValue() {
        return codedValue;
    }

    public int getValue() {
        return value;
    }
}
