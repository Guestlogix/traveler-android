package com.guestlogix.travelercorekit.models;

public class QuantityAnswer extends Answer {
    private int amount;

    public QuantityAnswer(int amount, Question question) {
        if (!(question.getType() instanceof QuestionType.Quantity)) {
            throw new IllegalArgumentException("Question must be of Quantity type");
        }

        questionId = question.getId();
        this.amount = amount;
    }

    @Override
    public String getCodedValue() {
        return String.valueOf(amount);
    }

    public int getValue() {
        return amount;
    }
}
