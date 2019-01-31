package com.guestlogix.travelercorekit.models;

import java.util.List;

public class MultipleChoiceType extends QuestionType {
    private List<Choice> choices;

    public MultipleChoiceType() {
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }
}
