package com.guestlogix.travelercorekit.models;

import com.guestlogix.travelercorekit.utilities.DateHelper;

import java.util.Calendar;

public class DateAnswer extends Answer {
    private Calendar value;

    public DateAnswer(Calendar value, Question question) {
        if (question.getType() != QuestionType.DATE) {
            throw new AnswerError();
        }

        this.value = value;
        this.questionId = question.getId();
    }

    @Override
    public String getCodedValue() {
        return DateHelper.formatDateToISO8601(value.getTime());
    }

    public Calendar getValue() {
        return value;
    }
}
