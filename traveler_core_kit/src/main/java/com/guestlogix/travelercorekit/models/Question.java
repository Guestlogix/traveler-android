package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
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
            String key = "Question";
            try {
                String id = "";
                String title = "";
                String description = "";
                QuestionType type = null;
                List<ValidationRule> rules = new ArrayList<>();
                Object options = null;

                List<Choice> choices = null;

                JsonToken token = reader.peek();
                if (JsonToken.NULL == token) {
                    reader.skipValue();
                    return null;
                }
                reader.beginObject();

                while (reader.hasNext()) {
                    key = reader.nextName();

                    switch (key) {
                        case "id":
                            id = JsonReaderHelper.readNonNullString(reader);
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
            } catch (IllegalArgumentException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.EMPTY_FIELD, String.format(e.getMessage(), key)));
            } catch (IOException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.INVALID_DATA, "IOException has occurred"));
            }
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

            throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.INVALID_DATA, "Payload type '" + type + "' is not yet supported"));
        }
    }
}
