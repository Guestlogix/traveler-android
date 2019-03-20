package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingException;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.IOException;
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
         * @param reader Object to read from
         * @return Question model
         * @throws ObjectMappingException If mapping fails.
         * @throws IOException            If mapping fails.
         */
        @Override
        public Question instantiate(JsonReader reader) throws ObjectMappingException, IOException {
            return readQuestion(reader);
        }

        /**
         * Parses a reader into a Question object.
         * <p>
         * Future considerations:
         * This method assumes that all extra fields are validation rules where the type is the name and the value is
         * whatever validation rule you need. For now it assumes that the value is a regex pattern. If later changed to
         * a more complicated object, this method will have to be changed.
         *
         * @param reader Object to read from
         * @return Question object
         * @throws IOException            If mapping cannot be done.
         * @throws ObjectMappingException If choice mapping fails.
         */
        private Question readQuestion(JsonReader reader) throws IOException, ObjectMappingException {
            String id = "";
            String title = "";
            String description = "";
            QuestionType type = null;
            List<ValidationRule> rules = new ArrayList<>();
            Object options = null;

            List<Choice> choices = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String name = reader.nextName();

                switch (name) {
                    case "id":
                        id = JsonReaderHelper.readString(reader);
                        break;
                    case "title":
                        title = JsonReaderHelper.readString(reader);
                        break;
                    case "description":
                        description = JsonReaderHelper.readString(reader);
                        break;
                    case "type":
                        type = determineQuestionType(JsonReaderHelper.readString(reader));
                        break;
                    case "choices":
                        ArrayMappingFactory<Choice> factory = new ArrayMappingFactory<>(new Choice.ChoiceObjectMappingFactory());
                        choices = factory.instantiate(reader);
                        break;
                    case "required":
                        Boolean required = JsonReaderHelper.readBoolean(reader);
                        if (required != null && required) {
                            rules.add(new RequiredValidationRule());
                        }
                        break;
//                    case "maximumQuantity":
//                        Integer max = JsonReaderHelper.readInteger(reader);
//                        if (max != null) {
//                            rules.add(new MaxQuantityValidationRule(max));
//                        }
//                        break;
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
                    // Add more question types here.
                }
            }

            throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.TYPE_NOT_SUPPORTED, "Payload type '" + type + "' is not yet supported"));
        }
    }
}
