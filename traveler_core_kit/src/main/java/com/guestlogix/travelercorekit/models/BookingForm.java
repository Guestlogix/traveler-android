package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.*;

/**
 * A BookingForm contains all the information required to book an experience.
 */
public class BookingForm implements Serializable {
    private Product product;
    private List<Pass> passes;
    private List<QuestionGroup> questionGroups;
    private Map<String, Answer> answers;

    private HashSet<String> questionIds;

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

    /**
     * Adds an answer for a question.
     *
     * @param answer answer for a question
     * @throws BookingFormError if the answer does not relate to any question
     */
    public void addAnswer(Answer answer) throws BookingFormError {
        if (!questionIds.contains(answer.questionId)) {
            throw new BookingFormError(BookingFormErrorCode.INVALID_QUESTION);
        }

        answers.put(answer.questionId, answer);
    }

    /**
     * Returns an answer to a question if it was set before.
     *
     * @param question question for which to get an answer
     * @return answer for a question if it exists, null otherwise
     * @throws BookingFormError if the question is not part of the booking form
     */
    @Nullable
    public Answer getAnswer(Question question) throws BookingFormError {
        if (!questionIds.contains(question.getId())) {
            throw new BookingFormError(BookingFormErrorCode.INVALID_QUESTION);
        }

        return answers.get(question.getId());
    }

    /**
     * Returns the passes used in the booking form.
     *
     * @return list of passes
     */
    public List<Pass> getPasses() {
        return this.passes;
    }

    /**
     * Product used in the booking form.
     *
     * @return product
     */
    public Product getProduct() {
        return product;
    }

    /**
     * returns all the answers in the booking form.
     *
     * @return all answers
     */
    public List<Answer> getAnswers() {
        return new ArrayList<>(answers.values());
    }

    /**
     * Returns all question groups
     *
     * @return all question groups in the form
     */
    @NonNull
    public List<QuestionGroup> getQuestionGroups() {
        return this.questionGroups;
    }

    /**
     * Performs an in order validation of all the questions in the booking form. Produces an in order list of all validation
     * errors found in the booking form. An empty list mean no errors were found.
     *
     * @return ordered list of errors in the form
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
    public class BookingFormError extends Error {
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