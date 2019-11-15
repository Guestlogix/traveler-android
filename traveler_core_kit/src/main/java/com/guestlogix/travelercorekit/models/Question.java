package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;

import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.DateHelper;
import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Question implements Serializable {
    private String id;
    private String title;
    private String description;
    private List<ValidationRule> validationRules;
    private QuestionType type;
    private SuggestedAnswer suggestedAnswer;

    Question(@NonNull String id, @NonNull String title, String description, @NonNull QuestionType type, @NonNull List<ValidationRule> rules, SuggestedAnswer suggestedAnswer) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.validationRules = rules;
        this.type = type;
        this.suggestedAnswer = suggestedAnswer;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public QuestionType getType() {
        return type;
    }

    List<ValidationRule> getValidationRules() {
        return validationRules;
    }

    public SuggestedAnswer getSuggestedAnswer() {
        return suggestedAnswer;
    }

    static class QuestionObjectMappingFactory implements ObjectMappingFactory<Question> {
        @Override
        public Question instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);

            String id = jsonObject.getString("id");
            String title = jsonObject.getString("title");
            String description = jsonObject.getNullableString("description");
            String typeString = jsonObject.getString("type");
            String suggestedAnswerString = jsonObject.getNullableString("suggestedAnswer");
            SuggestedAnswer suggestedAnswer = null;

            List<ValidationRule> rules = new ArrayList<>();
            if (jsonObject.getBoolean("required")) {
                rules.add(new RequiredValidationRule());
            }

            List<Choice> choices = new ArrayMappingFactory<>(new Choice.ChoiceObjectMappingFactory()).instantiate(jsonObject.getJSONArray("choices").toString());
            QuestionType type = null;

            Assertion.eval(id != null);
            Assertion.eval(title != null);
            Assertion.eval(typeString != null);

            switch (typeString) {
                case "MultipleChoice":
                    Assertion.eval(choices != null);
                    type = new QuestionType.MultipleChoice(choices);

                    if (suggestedAnswerString != null) {
                        int selectedIndex = -1;
                        for (int i = 0; i < choices.size(); i++) {
                            if (choices.get(i).getId().equals(suggestedAnswerString)) {
                                selectedIndex = i;
                                break;
                            }
                        }
                        if (selectedIndex == -1) {
                            throw new IllegalArgumentException("selected choice is not available in choices list");
                        }
                        suggestedAnswer = new SuggestedAnswer.MultipleChoiceSuggestedAnswer(selectedIndex);
                    }

                    break;
                case "Text":
                    type = new QuestionType.Textual();
                    if (suggestedAnswerString != null) {
                        suggestedAnswer = new SuggestedAnswer.TextualSuggestedAnswer(suggestedAnswerString);
                    }
                    break;
                case "Quantity":
                    type = new QuestionType.Quantity();
                    if (suggestedAnswerString != null) {
                        suggestedAnswer = new SuggestedAnswer.QuantitySuggestedAnswer(Integer.parseInt(suggestedAnswerString));
                    }
                    break;
                case "Date":
                    type = new QuestionType.Date();
                    if (suggestedAnswerString != null) {
                        suggestedAnswer = new SuggestedAnswer.DateSuggestedAnswer(DateHelper.parseISO8601(suggestedAnswerString));
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unknown Question type");
            }

            return new Question(id, title, description, type, rules, suggestedAnswer);
        }
    }
}
