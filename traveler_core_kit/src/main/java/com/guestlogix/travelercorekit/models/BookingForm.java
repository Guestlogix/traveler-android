package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BookingForm implements Serializable {
    private List<Pass> passes;

    private List<QuestionGroup> questionGroups;
    private List<AnswerGroup> answerGroups;
    private Map<Question, Integer> questionIndex;

    public BookingForm(List<Pass> passes) {
        this.passes = passes;

        questionIndex = new HashMap<>();
        questionGroups = new ArrayList<>();
        answerGroups = new ArrayList<>();

        // Init validators.
        ValidationRule required = new RequiredValidationRule();
        ValidationRule names = new PatternValidationRule("^[a-zA-Z ]*$");
        ValidationRule email = new PatternValidationRule("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$");
        ValidationRule phone = new PatternValidationRule("^\\d*$");


        List<Choice> choices = new ArrayList<>();
        choices.add(new Choice("Mr.", "Mr."));
        choices.add(new Choice("Mrs.", "Mrs."));
        Question q1 = createQuestion("title", "Title", null, QuestionType.MULTIPLE_CHOICE, null, choices, 0);

        List<ValidationRule> nameRules = new ArrayList<>();
        nameRules.add(required);
        nameRules.add(names);
        Question q2 = createQuestion("firstName", "First Name", null, QuestionType.STRING, nameRules, null, 0);
        Question q3 = createQuestion("lastName", "Last Name", null, QuestionType.STRING, nameRules, null, 0);

        List<ValidationRule> phoneRules = new ArrayList<>();
        phoneRules.add(required);
        phoneRules.add(phone);
        Question q4 = createQuestion("phone", "Phone number", null, QuestionType.STRING, phoneRules, null, 0);

        List<ValidationRule> emailRules = new ArrayList<>();
        emailRules.add(required);
        emailRules.add(email);
        Question q5 = createQuestion("email", "Email", null, QuestionType.STRING, emailRules, null, 0);

        List<Question> primaryQuestions = new ArrayList<>();
        primaryQuestions.add(q1);
        primaryQuestions.add(q2);
        primaryQuestions.add(q3);
        primaryQuestions.add(q4);
        primaryQuestions.add(q5);

        QuestionGroup clientQuestions = new QuestionGroup("Primary Contact", "The primary contact is the person that will receive purchase confirmation and passes. This person does not have to be part of the group", primaryQuestions);

        questionGroups.add(clientQuestions);
        answerGroups.add(new AnswerGroup("client questions"));

        for (int i = 0; i < passes.size(); i++) {
            answerGroups.add(new AnswerGroup(passes.get(i).getId()));

            List<Question> questions = new ArrayList<>();

            for (Question q : passes.get(i).getQuestions()) {
                questions.add(createQuestion(q.getId(), q.getTitle(), q.getDescription(), q.getType(), q.getValidationRules(), q.getOptions(), i));
            }

            String title = String.format("%s - %s", passes.get(i).getName(), i + 1);
            QuestionGroup q = new QuestionGroup(title, null, questions);
            questionGroups.add(q);
        }
    }

    public void addAnswer(Answer answer, Question question) throws IllegalArgumentException {
        Integer i = questionIndex.get(question);

        if (i == null) {
            throw new IllegalArgumentException("Question is not part of the form");
        }

        answerGroups.get(i).addAnswer(answer, question.getId());
    }

    @Nullable
    public Answer getAnswer(Question question) throws IllegalArgumentException {
        Integer i = questionIndex.get(question);

        if (i == null) {
            throw new IllegalArgumentException("Question is not part of the form");
        }

        return answerGroups.get(i).answers.get(question.getId());
    }

    @Nullable
    public List<Answer> getAnswers(Pass pass) {
        for (AnswerGroup a : answerGroups) {
            if (a.passId.equals(pass.getId())) {
                return new ArrayList<>(a.answers.values());
            }
        }

        return null;
    }

    public List<Pass> getPasses() {
        return this.passes;
    }

    @NonNull
    public List<QuestionGroup> getQuestionGroups() {
        return this.questionGroups;
    }

    /**
     * Validates all questions in the form and returns an ordered list of all the errors. Empty list means no errors were
     * found in the form.
     *
     * @return List of errors in the form.
     */
    @NonNull
    public List<BookingFormError> validate() {
        List<BookingFormError> errors = new ArrayList<>();

        for (int i = 0; i < questionGroups.size(); i++) {
            for (int j = 0; j < questionGroups.get(i).getQuestions().size(); j++) {
                Question question = questionGroups.get(i).getQuestions().get(j);
                Answer answer = answerGroups.get(i).answers.get(question.getId());

                if (question.getValidationRules() != null) {
                    for (ValidationRule r : question.getValidationRules()) {
                        if (!r.validate(answer)) {

                            BookingFormErrorType errorType = null;
                            switch (r.error) {
                                case REQUIRED:
                                    errorType = BookingFormErrorType.REQUIRED;
                                    break;
                                case REGEX_MISMATCH:
                                    errorType = BookingFormErrorType.INCORRECT_PATTERN;
                                    break;
                                case BAD_QUANTITY:
                                    errorType = BookingFormErrorType.INCORRECT_QUANTITY;
                                    break;
                            }

                            BookingFormError error = new BookingFormError(i, j, errorType);
                            errors.add(error);
                        }
                    }
                }
            }
        }

        return errors;
    }

    private Question createQuestion(String id, String title, String subtitle, QuestionType type, List<ValidationRule> rules, Object options, int i) {
        Question question = new Question(id, title, subtitle, type, rules, options);
        questionIndex.put(question, i);
        return question;
    }

    /**
     * Types of errors which can be encountered while validating the form.
     */
    public enum BookingFormErrorType {
        REQUIRED, INCORRECT_PATTERN, INCORRECT_QUANTITY
    }

    /**
     * Booking form errors.
     * Contains the relative position in the form of the question which contains errors.
     */
    public class BookingFormError {
        public int groupId;
        public int questionId;
        public BookingFormErrorType error;

        BookingFormError(int groupId, int questionId, BookingFormErrorType error) {
            this.groupId = groupId;
            this.questionId = questionId;
            this.error = error;
        }
    }

    private class AnswerGroup implements Serializable {
        Map<String, Answer> answers;
        String passId;

        AnswerGroup(String passId) {
            this.passId = passId;
            this.answers = new HashMap<>();
        }

        void addAnswer(Answer a, String id) {
            answers.put(id, a);
        }
    }
}