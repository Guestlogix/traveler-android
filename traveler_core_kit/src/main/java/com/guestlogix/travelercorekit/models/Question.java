package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import androidx.annotation.NonNull;
import com.guestlogix.travelercorekit.utilities.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Question implements Serializable {
    private String id;
    private String title;
    private String description;
    private List<ValidationRule> validationRules;
    private QuestionType type;

    Question(@NonNull String id, @NonNull String title, String description, @NonNull QuestionType type, @NonNull List<ValidationRule> rules) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.validationRules = rules;
        this.type = type;
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

    static class QuestionObjectMappingFactory implements ObjectMappingFactory<Question> {
        @Override
        public Question instantiate(JsonReader reader) throws Exception {
            String id = null;
            String title = null;
            String description = null;
            String typeString = null;
            List<ValidationRule> rules = new ArrayList<>();
            List<Choice> choices = null;
            QuestionType type = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "id":
                        id = reader.nextString();
                        break;
                    case "title":
                        title = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "description":
                        description = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "type":
                        typeString = reader.nextString();
                        break;
                    case "choices":
                        ArrayMappingFactory<Choice> factory = new ArrayMappingFactory<>(new Choice.ChoiceObjectMappingFactory());
                        choices = factory.instantiate(reader);
                        break;
                    case "required":
                        if (reader.nextBoolean())
                            rules.add(new RequiredValidationRule());
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }

            reader.endObject();

            Assertion.eval(id != null);
            Assertion.eval(title != null);
            Assertion.eval(typeString != null);

            switch (typeString) {
                case "MultipleChoice":
                    Assertion.eval(choices != null);
                    type = new QuestionType.MultipleChoice(choices);
                    break;
                case "Text":
                    type = new QuestionType.Textual();
                    break;
                case "Quantity":
                    type = new QuestionType.Quantity();
                    break;
                case "Date":
                    type = new QuestionType.Date();
                    break;
                default:
                    throw new IllegalArgumentException("Unknown Question type");
            }

            return new Question(id, title, description, type, rules);
        }
    }
}
