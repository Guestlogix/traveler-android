package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import com.guestlogix.travelercorekit.error.TravelerError;
import com.guestlogix.travelercorekit.error.TravelerErrorCode;
import com.guestlogix.travelercorekit.network.ArrayMappingFactory;
import com.guestlogix.travelercorekit.network.ObjectMappingException;
import com.guestlogix.travelercorekit.network.ObjectMappingFactory;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.validators.MaxQuantityValidationRule;
import com.guestlogix.travelercorekit.validators.PatternValidationRule;
import com.guestlogix.travelercorekit.validators.RequiredValidationRule;
import com.guestlogix.travelercorekit.validators.ValidationRule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Question {
    private String id;
    private String title;
    private String description;
    private QuestionType type;
    private List<ValidationRule> validationRules;

    public Question(String id, String title, String description, QuestionType type, List<ValidationRule> rules) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.validationRules = rules;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public List<ValidationRule> getValidationRules() {
        return validationRules;
    }

    public void setValidationRules(List<ValidationRule> validationRules) {
        this.validationRules = validationRules;
    }

    public static class QuestionObjectMappingFactory implements ObjectMappingFactory<Question> {

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

            List<Choice> choices = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String name = reader.nextName();

                switch (name) {
                    case "id":
                        id = JsonReaderHelper.readString(reader);
                        break;
                    case "name":
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
                    default:
                        ValidationRule rule = readValidationRule(name, reader);
                        if (null != rule) {
                            rules.add(rule);
                        }
                        break;
                }
            }
            reader.endObject();

            // Add choices if it's a multiple choice question type.
            if (type instanceof MultipleChoiceType) {
                ((MultipleChoiceType) type).setChoices(choices);
            }

            return new Question(id, title, description, type, rules);
        }

        private QuestionType determineQuestionType(String type) throws ObjectMappingException {
            QuestionType questionType = null;

            if (null != type) {
                if (type.equals("MultipleChoice")) {
                    questionType = new MultipleChoiceType();
                } else if (type.equals("Text")) {
                    questionType = new StringType();
                } else if (type.equals("Quantity")) {
                    questionType = new QuantityType();
                }
                // Add more question types here.
            }

            throw new ObjectMappingException(new TravelerError(TravelerErrorCode.PARSING_ERROR, "Invalid json. Unsupported question type."));
        }

        /**
         * Determines which validation rules to create.
         * <p>
         * Thoughts:
         * For now, the validation rule mapping is trivial so this method can be placed here. However, if the validation
         * becomes more complicated, it would be better to extract this logic into a standalone class.
         *
         * @param name   JSON field.
         * @param reader reader object.
         * @return A validation rule.
         * @throws IOException if there was a problem reading the validation rule.
         */
        private ValidationRule readValidationRule(String name, JsonReader reader) throws IOException {
            if (name.equals("required")) {
                Boolean required = JsonReaderHelper.readBoolean(reader);

                if (null != required && required) {
                    return new RequiredValidationRule();
                }
            } else if (name.equals("maximumQuantity")) {
                Integer max = JsonReaderHelper.readInteger(reader);

                if (null != max && max >= 0) {
                    return new MaxQuantityValidationRule(max);
                }
            } else {
                String pattern = JsonReaderHelper.readString(reader);

                if (null != pattern) {
                    return new PatternValidationRule(pattern);
                }
            }
            return null;
        }
    }
}
