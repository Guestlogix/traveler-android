package com.guestlogix.travelercorekit.models;

public class MultipleChoiceSelection extends Answer {
    private int value;

    public MultipleChoiceSelection(int value, Question question) throws IllegalArgumentException {
        QuestionType type = question.getType();

        if (!(type instanceof QuestionType.MultipleChoice)) {
            throw new IllegalArgumentException("Question must be of MultipleChoice type");
        }

        QuestionType.MultipleChoice multipleChoice = (QuestionType.MultipleChoice) type;

        if (value < 0 || value >= multipleChoice.getChoices().size()) {
            throw new IllegalArgumentException("Answer must be one of available choices");
        }

        this.value = value;
        this.codedValue = multipleChoice.getChoices().get(value).getId();
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
