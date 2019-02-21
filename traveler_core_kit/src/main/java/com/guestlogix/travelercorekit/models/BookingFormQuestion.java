package com.guestlogix.travelercorekit.models;


import com.guestlogix.travelercorekit.validators.ValidationRule;
import java.util.List;

class BookingFormQuestion extends Question {
    int index;

    public BookingFormQuestion(String id, String title, String description, QuestionType type, List<ValidationRule> rules,
                               Object options, int index) {
        super(id, title, description, type, rules, options);

        this.index = index;
    }

    @Override
    public int hashCode() {
        String hash = getId() + "-" + index;
        return hash.hashCode();
    }
}
