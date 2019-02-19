package com.guestlogix.travelercorekit.models;

import androidx.annotation.Nullable;
import com.guestlogix.travelercorekit.validators.ValidationRule;

import java.util.ArrayList;
import java.util.List;

public class BookingFormQuestion {
    private String id;
    private String title;
    private QuestionType type;
    private List<ValidationRule> validationRules;

    public BookingFormQuestion(String id, String title, QuestionType type, @Nullable List<ValidationRule> validationRules) {
        this.id = id;
        this.title = title;
        this.type = type;

        if (validationRules == null) {
            this.validationRules = new ArrayList<>();
        } else {
            this.validationRules = validationRules;
        }
    }

    public String getTitle() {
        return title;
    }

    public QuestionType getType() {
        return type;
    }

    public List<ValidationRule> getValidationRules() {
        return validationRules;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof BookingFormQuestion)) {
            return false;
        }

        final BookingFormQuestion q = (BookingFormQuestion) obj;

        return this.id.equals(q.id);
    }
}
