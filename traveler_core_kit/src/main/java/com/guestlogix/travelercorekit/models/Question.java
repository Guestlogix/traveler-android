package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import com.guestlogix.travelercorekit.network.ArrayMappingFactory;
import com.guestlogix.travelercorekit.network.ObjectMappingException;
import com.guestlogix.travelercorekit.network.ObjectMappingFactory;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
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
    private Integer maxQuantity;
    private QuestionType type;
    private List<ValidationRule> validationRules;

    public Question(String id, String title, String description, Integer maxQuantity, QuestionType type, List<ValidationRule> rules) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.maxQuantity = maxQuantity;
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

    public Integer getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(Integer maxQuantity) {
        this.maxQuantity = maxQuantity;
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
            return null;
        }

        /**
         * Parses a reader into a Question object.
         * <p>
         * Future considerations:
         * This method assumes that all extra fields are validation rules where the type is the
         * name and the value is whatever validation rule you need. For now it assumes that the
         * value is a regex pattern. If later changed to a more complicated object, this method
         * will have to be changed.
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
            Integer maxQuantity = null;
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
                    case "title":
                        title = JsonReaderHelper.readString(reader);
                        break;
                    case "description":
                        description = JsonReaderHelper.readString(reader);
                        break;
                    case "maximumQuantity":
                        maxQuantity = JsonReaderHelper.readInteger(reader);
                        break;
                    case "type":
                        type = determineQuestionType(JsonReaderHelper.readString(reader));
                        break;
                    case "choices":
                        ArrayMappingFactory<Choice> factory = new ArrayMappingFactory<>(new Choice.ChoiceObjectMappingFactory());
                        choices = factory.instantiate(reader);
                        break;
                    default:
                        ValidationRule rule = readValdiationRule(name, reader);
                        if (null != rule) {
                            rules.add(rule);
                        }

                }
            }
            reader.endObject();

            // Add choices if its a multiple choice question type.
            if (type instanceof MultipleChoiceType) {
                ((MultipleChoiceType) type).setChoices(choices);
            }

            return new Question(id, title, description, maxQuantity, type, rules);
        }

        private QuestionType determineQuestionType(String type) {
            QuestionType questionType = null;

            if (null != type) {
                if (type.equals("MultipleChoice")) {
                    questionType = new MultipleChoiceType();
                } else if (type.equals("Text")) {
                    questionType = new StringType();
                }
                // Add more question types here.
            }

            return questionType;
        }

        private ValidationRule readValdiationRule(String name, JsonReader reader) throws IOException {
            if (name.equals("required")) {
                Boolean required = JsonReaderHelper.readBoolean(reader);

                if (null != required && required) {
                    return new RequiredValidationRule();
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
