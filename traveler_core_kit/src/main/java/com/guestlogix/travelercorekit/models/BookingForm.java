package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.*;


public class BookingForm implements Serializable {
    private List<Pass> passes;
    private List<QuestionGroup> questionGroups;
    private Product product;
    private Map<String, Answer> answers;

    private transient HashSet<String> questionIds;

    BookingForm(Product product, List<Pass> passes, List<QuestionGroup> questionGroups) {
        this.passes = passes;
        this.questionGroups = questionGroups;
        this.product = product;

        // So we don't have to loop through all questions every time we add or get an answer.
        questionIds = new HashSet<>();
        for (QuestionGroup group : questionGroups) {
            for (Question question : group.getQuestions()) {
                questionIds.add(question.getId());
            }
        }

        answers = new HashMap<>();
    }

    public void addAnswer(Answer answer) throws BookingFormError {
        if (!questionIds.contains(answer.questionId)) {
            throw new BookingFormError(BookingFormErrorCode.INVALID_QUESTION);
        }

        answers.put(answer.questionId, answer);
    }

    @Nullable
    public Answer getAnswer(Question question) throws BookingFormError {
        if (!questionIds.contains(question.getId())) {
            throw new BookingFormError(BookingFormErrorCode.INVALID_QUESTION);
        }

        return answers.get(question.getId());
    }

    public List<Pass> getPasses() {
        return this.passes;
    }

    public Product getProduct() {
        return product;
    }

    public List<Answer> getAnswers() {
        return new ArrayList<>(answers.values());
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
                Answer answer = answers.get(question.getId());

                if (question.getValidationRules() != null) {
                    for (ValidationRule r : question.getValidationRules()) {
                        if (!r.validate(answer)) {
                            errors.add(new BookingFormError(i, j, r.error));
                        }
                    }
                }
            }
        }

        return errors;
    }

    /**
     * Types of errors which can be encountered while validating the form.
     */
    public enum BookingFormErrorCode {
        INVALID_QUESTION, INVALID_ANSWER
    }

    /**
     * Booking form errors.
     * Contains the relative position in the form of the question which contains errors.
     */
    public class BookingFormError extends Error{
        private int groupId;
        private int questionId;
        private ValidationError error;
        private BookingFormErrorCode type;

        private BookingFormError(int groupId, int questionId, ValidationError error) {
            this.groupId = groupId;
            this.questionId = questionId;
            this.error = error;
            type = BookingFormErrorCode.INVALID_ANSWER;
        }

        BookingFormError(BookingFormErrorCode type) {
            this.type = type;
        }

        public int getGroupId() {
            return groupId;
        }

        public int getQuestionId() {
            return questionId;
        }

        public ValidationError getError() {
            return error;
        }

        public BookingFormErrorCode getType() {
            return type;
        }
    }
}