package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingException;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Question implements Serializable {
    private String id;
    private String title;
    private String description;
    private QuestionType type;
    private List<ValidationRule> validationRules;
    private Object options;

    Question(String id, String title, String description, QuestionType type, List<ValidationRule> rules, Object options) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.validationRules = rules;
        this.options = options;
    }

    public Object getOptions() {
        return options;
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
        /**
         * Parses a json reader into a Question model. Does not guarantee correctness of the values when parsing the json.
         *
         * @param reader object to read from.
         * @return Question model.
         * @throws {@link Exception} if mapping fails due to unexpected token, invalid type, missing required field or unable to parse date type.
         */
        @Override
        public Question instantiate(JsonReader reader) throws Exception {
            String key;
            String id = "";
            String title = "";
            String description = "";
            QuestionType type = null;
            List<ValidationRule> rules = new ArrayList<>();
            Object options = null;

            List<Choice> choices = null;

            reader.beginObject();

            while (reader.hasNext()) {
                key = reader.nextName();

                switch (key) {
                    case "id":
                        id = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "title":
                        title = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "description":
                        description = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "type":
                        type = determineQuestionType(reader.nextString());
                        break;
                    case "choices":
                        if (JsonToken.NULL != reader.peek()) {
                            choices = new ArrayMappingFactory<>(new Choice.ChoiceObjectMappingFactory()).instantiate(reader);
                        } else {
                            reader.skipValue();
                        }
                        break;
                    case "required":
                        boolean required = reader.nextBoolean();
                        if (required) {
                            rules.add(new RequiredValidationRule());
                        }
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }

            reader.endObject();

            // Add choices if it's a multiple choice question type.
            if (type == QuestionType.MULTIPLE_CHOICE) {
                options = choices;
            }

            return new Question(id, title, description, type, rules, options);
        }

        private QuestionType determineQuestionType(String type) throws ObjectMappingException {

            if (null != type) {
                switch (type) {
                    case "MultipleChoice":
                        return QuestionType.MULTIPLE_CHOICE;
                    case "Text":
                        return QuestionType.STRING;
                    case "Quantity":
                        return QuestionType.QUANTITY;
                    case "Date":
                        return QuestionType.DATE;
                    // Add more question types here.
                }
            }

            throw new IllegalArgumentException("Payload type '" + type + "' is not yet supported");
        }
    }
}
