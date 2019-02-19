package com.guestlogix.travelercorekit.models;

import android.annotation.SuppressLint;
import android.util.SparseArray;
import com.guestlogix.travelercorekit.validators.PatternValidationRule;
import com.guestlogix.travelercorekit.validators.RequiredValidationRule;
import com.guestlogix.travelercorekit.validators.ValidationError;
import com.guestlogix.travelercorekit.validators.ValidationRule;

import java.util.ArrayList;
import java.util.List;


public class BookingForm {
    private List<QuestionGroup> questionGroups;
    private SparseArray<Answer> answers;

    public BookingForm(List<Pass> passes) {
        questionGroups = new ArrayList<>();
        answers = new SparseArray<>();

        // Required questions.
        questionGroups.add(buildDefault());

        // Copy questions for each pass and assign other id.
        for (int i = 0; i < passes.size(); i++) {
            Pass p = passes.get(i);

            List<Question> questions = makeQuestions(p, i);
            QuestionGroup questionGroup = new QuestionGroup(
                    String.format("Guest %d: %s", questionGroups.size(), p.getName()),
                    null,
                    questions);

            questionGroups.add(questionGroup);
        }
    }

    public void addAnswer(Answer answer) {
        containsOrError(answer.questionId);
        answers.append(answer.questionId.hashCode(), answer);
    }

    public Answer getAnswer(Question question) {
        containsOrError(question.getId());
        return answers.get(question.getId().hashCode());
    }

    public List<BookingFormError> validate() {
        List<BookingFormError> errors = new ArrayList<>();

        for (int i = 0; i < questionGroups.size(); i++) {
            for (int j = 0; j < questionGroups.get(i).getQuestions().size(); j++) {
                Question question = questionGroups.get(i).getQuestions().get(j);
                Answer answer = answers.get(question.getId().hashCode());

                for (ValidationRule validationRule : question.getValidationRules()) {
                    boolean isValid = validationRule.validate(answer);

                    if (!isValid) {
                        errors.add(new BookingFormError(i, j, validationRule.getError()));
                    }
                }
            }
        }

        return errors;
    }

    // Checks if id is in the questions else throws error.
    private void containsOrError(String id) {
        for (int i = 0; i < questionGroups.size(); i++) {
            for (int j = 0; j < questionGroups.get(i).getQuestions().size(); j++) {
                if (questionGroups.get(i).getQuestions().get(j).getId().equals(id)) {
                    return;
                }
            }
        }

        throw new Error(); // TODO: Change this.
    }

    // TODO: Extract default string literals into a resource.
    private QuestionGroup buildDefault() {
        // Init validators.
        ValidationRule required = new RequiredValidationRule();
        ValidationRule names = new PatternValidationRule("^[a-zA-Z ]*$");
        ValidationRule email = new PatternValidationRule("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$");
        ValidationRule phone = new PatternValidationRule("^\\d*$");


        Question q1 = new Question("title", "Title", null, new MultipleChoiceType(), null);

        List<ValidationRule> nameRules = new ArrayList<>();
        nameRules.add(required);
        nameRules.add(names);
        Question q2 = new Question("firstName", "First Name", null, new StringType(), nameRules);
        Question q3 = new Question("lastName", "Last Name", null, new StringType(), nameRules);

        List<ValidationRule> phoneRules = new ArrayList<>();
        phoneRules.add(required);
        phoneRules.add(phone);
        Question q4 = new Question("phone", "Phone number", null, new StringType(), phoneRules);

        List<ValidationRule> emailRules = new ArrayList<>();
        emailRules.add(required);
        emailRules.add(email);
        Question q5 = new Question("email", "Email", null, new StringType(), emailRules);

        List<Question> primaryQuestions = new ArrayList<>();
        primaryQuestions.add(q1);
        primaryQuestions.add(q2);
        primaryQuestions.add(q3);
        primaryQuestions.add(q4);
        primaryQuestions.add(q5);

        return new QuestionGroup("Primary Contact", "The primary contact is the person that will receive purchase confirmation and passes. This person does not have to be part of the group", primaryQuestions);
    }

    @SuppressLint("DefaultLocale")
    private List<Question> makeQuestions(Pass p, int index) {
        List<Question> questions = new ArrayList<>();

        for (Question q : p.getQuestions()) {
            questions.add(new Question(String.format("%s-%d", q.getId(), index), q.getTitle(), q.getDescription(), q.getType(), q.getValidationRules()));
        }

        return questions;
    }

    public class BookingFormError extends Error {
        public final int groupIndex;
        public final int questionIndex;
        public final ValidationError error;

        public BookingFormError(int groupIndex, int questionIndex, ValidationError error) {
            this.groupIndex = groupIndex;
            this.questionIndex = questionIndex;
            this.error = error;
        }
    }
}
