package com.guestlogix.travelercorekit.models;

import com.guestlogix.travelercorekit.utilities.DateHelper;

import java.util.Calendar;
import java.util.Date;

public class DateAnswer extends Answer {
    private Date value;

    public DateAnswer(Date value, Question question) {
        if (!(question.getType() instanceof QuestionType.Date)) {
            throw new IllegalArgumentException("Question must be of Date type");
        }

        this.value = value;
        this.questionId = question.getId();
    }

    @Override
    public String getCodedValue() {
        return DateHelper.formatDateToISO8601(value);
    }

    public Date getValue() {
        return value;
    }
}
