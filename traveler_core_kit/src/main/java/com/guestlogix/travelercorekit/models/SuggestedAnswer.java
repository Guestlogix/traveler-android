package com.guestlogix.travelercorekit.models;

import java.io.Serializable;
import java.util.Date;

public abstract class SuggestedAnswer<T> implements Serializable {

    protected T value;

    public T getValue() {
        return value;
    }


// Types

    public static class TextualSuggestedAnswer extends SuggestedAnswer<String> {
        TextualSuggestedAnswer(String value) {
            this.value = value;
        }
    }

    public static class QuantitySuggestedAnswer extends SuggestedAnswer<Integer> {
        QuantitySuggestedAnswer(int value) {
            this.value = value;
        }
    }

    public static class MultipleChoiceSuggestedAnswer extends SuggestedAnswer<Integer> {
        MultipleChoiceSuggestedAnswer(int selectedIndex) {
            this.value = selectedIndex;
        }
    }

    public static class DateSuggestedAnswer extends SuggestedAnswer<Date> {
        DateSuggestedAnswer(java.util.Date value) {
            this.value = value;
        }
    }

}
