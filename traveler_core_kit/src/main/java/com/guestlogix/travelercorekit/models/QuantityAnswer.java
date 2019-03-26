package com.guestlogix.travelercorekit.models;

public class QuantityAnswer extends Answer {
    private int amount;

    public QuantityAnswer(int amount, Question question) {
        if (question.getType() != QuestionType.QUANTITY) {
            throw new AnswerError();
        }

        questionId = question.getId();
        this.amount = amount;
    }

    @Override
    public String getCodedValue() {
        return String.valueOf(amount);
    }

    public int getAmount() {
        return amount;
    }
}
