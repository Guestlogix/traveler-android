package com.guestlogix.travelercorekit.models;

import java.util.List;

public class MultipleChoiceSelection extends Answer {
    private int value;

    public MultipleChoiceSelection(int value, Question question) {
        if (question.getType() != QuestionType.MULTIPLE_CHOICE || question.getOptions() == null) {
            // Throw exception;
            throw new AnswerError();
        }

        // MC question with non null options.
        List<Choice> choices = (List<Choice>) question.getOptions();

        if (value < 0 || value >= choices.size()) {
            throw new AnswerError();
        }

        this.value = value;
        this.codedValue = choices.get(value).getId();
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
