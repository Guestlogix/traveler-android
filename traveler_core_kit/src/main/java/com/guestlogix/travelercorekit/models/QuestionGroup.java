package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import androidx.annotation.NonNull;
import com.guestlogix.travelercorekit.utilities.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class QuestionGroup implements Serializable {
    private String title;
    private String disclaimer;
    private List<Question> questions;

    private QuestionGroup(String title, String disclaimer, @NonNull List<Question> questions) {
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
            String title = null;
            String disclaimer = null;
            List<Question> questions = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "title":
                        title = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "description":
                        disclaimer = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "questions":
                        questions = new ArrayMappingFactory<>(new Question.QuestionObjectMappingFactory()).instantiate(reader);
                        break;
                    default:
                        reader.skipValue();
                }
            }

            reader.endObject();

            Assertion.eval(questions != null);

            return new QuestionGroup(title, disclaimer, questions);
        }
    }
}
