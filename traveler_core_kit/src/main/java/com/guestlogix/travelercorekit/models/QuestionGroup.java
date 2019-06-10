package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.Serializable;
import java.util.List;

public class QuestionGroup implements Serializable {
    private String title;
    private String disclaimer;
    private List<Question> questions;

    private QuestionGroup(String title, String disclaimer, List<Question> questions) throws IllegalArgumentException {
        if (questions == null) {
            throw new IllegalArgumentException("questions can not be null");
        }

        this.title = title;
        this.disclaimer = disclaimer;
        this.questions = questions;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public String getTitle() {
        return title;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    static class QuestionGroupObjectMappingFactory implements ObjectMappingFactory<QuestionGroup> {
        /**
         * Parses a reader object into QuestionGroup model.
         *
         * @param reader object to parse from.
         * @return QuestionGroup model object from the reader.
         * @throws {@link Exception} if mapping fails due to unexpected token, invalid type, missing required field or unable to parse date type.
         */
        @Override
        public QuestionGroup instantiate(JsonReader reader) throws Exception {
            String key;
            String title = null;
            String disclaimer = null;
            List<Question> questions = null;

            reader.beginObject();

            while (reader.hasNext()) {
                key = reader.nextName();

                switch (key) {
                    case "title":
                        title = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "description":
                        disclaimer = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "questions":
                        if (JsonToken.NULL != reader.peek()) {
                            questions = new ArrayMappingFactory<>(new Question.QuestionObjectMappingFactory()).instantiate(reader);
                        } else {
                            questions = null;
                            reader.skipValue();
                        }

                        break;
                    default:
                        reader.skipValue();
                }
            }

            reader.endObject();

            Assertion.eval(null != questions, "questions can not be null");

            return new QuestionGroup(title, disclaimer, questions);
        }
    }
}
