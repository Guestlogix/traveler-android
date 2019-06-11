package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import com.guestlogix.travelercorekit.utilities.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class QuestionGroup implements Serializable {
    private String title;
    private String disclaimer;
    private List<Question> questions;

    private QuestionGroup(String title, String disclaimer, List<Question> questions) {
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

        @Override
        public QuestionGroup instantiate(JsonReader reader) throws Exception {
            String key = "QuestionGroup";
            try {
                String title = null;
                String disclaimer = null;
                List<Question> questions = null;

                JsonToken token = reader.peek();
                if (JsonToken.NULL == token) {
                    reader.skipValue();
                    return null;
                }
                reader.beginObject();

                while (reader.hasNext()) {
                    key = reader.nextName();

                    switch (key) {
                        case "title":
                            title = JsonReaderHelper.readString(reader);
                            break;
                        case "description":
                            disclaimer = JsonReaderHelper.readString(reader);
                            break;
                        case "questions":
                            questions = new ArrayMappingFactory<>(new Question.QuestionObjectMappingFactory()).instantiate(reader);
                            break;
                        default:
                            reader.skipValue();
                    }
                }

                reader.endObject();

                if (questions == null) {
                    throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.EMPTY_FIELD, "QuestionGroup 'questions' is a required json field."));
                }

                return new QuestionGroup(title, disclaimer, questions);
            } catch (IllegalArgumentException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.EMPTY_FIELD, String.format(e.getMessage(), key)));
            } catch (IOException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.INVALID_DATA, "IOException has occurred"));
            }
        }
    }
}
