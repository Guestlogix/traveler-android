package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;

import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;

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
        public QuestionGroup instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);

            String title = jsonObject.getNullableString("title");
            String disclaimer = jsonObject.getNullableString("subTitle");
            List<Question> questions = new ArrayMappingFactory<>(new Question.QuestionObjectMappingFactory()).instantiate(jsonObject.getJSONArray("questions").toString());

            Assertion.eval(questions != null);

            return new QuestionGroup(title, disclaimer, questions);
        }
    }
}
