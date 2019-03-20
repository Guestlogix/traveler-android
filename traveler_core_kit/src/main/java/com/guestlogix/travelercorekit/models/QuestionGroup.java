package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingException;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

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
        public QuestionGroup instantiate(JsonReader reader) throws ObjectMappingException, IOException {
            String title = null;
            String disclaimer = null;
            List<Question> questions = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String name = reader.nextName();

                switch (name) {
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
        }
    }
}
