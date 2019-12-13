package com.guestlogix.travelercorekit.models;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * A PurchaseForm contains all the information required to book an experience.
 */
public class PurchaseForm implements Serializable {
    private Product product;
    //TODO: this is not OOP. some one can send a pass as productOffering for partner offering purchase form which is wrong. we should have 3 separate purchase form :"( please fix this
    private List<ProductOffering> productOfferings;
    private List<QuestionGroup> questionGroups;
    private Map<String, Answer> answers;

    private HashSet<String> questionIds;

    PurchaseForm(Product product, List<ProductOffering> productOfferings, List<QuestionGroup> questionGroups) {
        this.productOfferings = productOfferings;
        this.questionGroups = questionGroups;
        this.product = product;

        // used a hashset so we don't have to loop through all questions every time we add or get an answer.
        questionIds = new HashSet<>();
        answers = new HashMap<>();

        for (QuestionGroup group : questionGroups) {
            for (Question question : group.getQuestions()) {
                questionIds.add(question.getId());

                if (question.getSuggestedAnswer() != null) {
                    if (question.getSuggestedAnswer() instanceof SuggestedAnswer.MultipleChoiceSuggestedAnswer) {
                        addAnswer(new MultipleChoiceSelection(((SuggestedAnswer.MultipleChoiceSuggestedAnswer) question.getSuggestedAnswer()).getValue(), question));
                    } else if (question.getSuggestedAnswer() instanceof SuggestedAnswer.TextualSuggestedAnswer) {
                        addAnswer(new TextualAnswer(((SuggestedAnswer.TextualSuggestedAnswer) question.getSuggestedAnswer()).getValue(), question));
                    } else if (question.getSuggestedAnswer() instanceof SuggestedAnswer.QuantitySuggestedAnswer) {
                        addAnswer(new QuantityAnswer(((SuggestedAnswer.QuantitySuggestedAnswer) question.getSuggestedAnswer()).getValue(), question));
                    } else if (question.getSuggestedAnswer() instanceof SuggestedAnswer.DateSuggestedAnswer) {
                        addAnswer(new DateAnswer(((SuggestedAnswer.DateSuggestedAnswer) question.getSuggestedAnswer()).getValue(), question));
                    } else {
                        Log.e("purchase form", "the suggested answer type is invalid");
                    }
                }

            }
        }
    }

    /**
     * Adds an answer for a question.
     *
     * @param answer answer for a question
     * @throws PurchaseFormError if the answer does not relate to any question
     */
    public void addAnswer(Answer answer) throws PurchaseFormError {
        if (!questionIds.contains(answer.questionId)) {
            throw new PurchaseFormError(PurchaseFormErrorCode.INVALID_QUESTION);
        }

        answers.put(answer.questionId, answer);
    }

    /**
     * Returns an answer to a question if it was set before.
     *
     * @param question question for which to get an answer
     * @return answer for a question if it exists, null otherwise
     * @throws PurchaseFormError if the question is not part of the booking form
     */
    @Nullable
    public Answer getAnswer(Question question) throws PurchaseFormError {
        if (!questionIds.contains(question.getId())) {
            throw new PurchaseFormError(PurchaseFormErrorCode.INVALID_QUESTION);
        }

        return answers.get(question.getId());
    }

    /**
     * Returns the productOfferings used in the booking form.
     *
     * @return list of productOfferings
     */
    public List<ProductOffering> getProductOfferings() {
        return this.productOfferings;
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
    public List<PurchaseFormError> validate() {
        List<PurchaseFormError> errors = new ArrayList<>();

        for (int i = 0; i < questionGroups.size(); i++) {
            for (int j = 0; j < questionGroups.get(i).getQuestions().size(); j++) {
                Question question = questionGroups.get(i).getQuestions().get(j);
                Answer answer = answers.get(question.getId());

                if (question.getValidationRules() != null) {
                    for (ValidationRule r : question.getValidationRules()) {
                        if (!r.validate(answer)) {
                            errors.add(new PurchaseFormError(i, j, r.error));
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
    public enum PurchaseFormErrorCode {
        INVALID_QUESTION, INVALID_ANSWER
    }

    /**
     * Purchase form errors.
     * Contains the relative position in the form of the question which contains errors.
     */
    public class PurchaseFormError extends Error {
        private int groupId;
        private int questionId;
        private ValidationError error;
        private PurchaseFormErrorCode type;

        private PurchaseFormError(int groupId, int questionId, ValidationError error) {
            this.groupId = groupId;
            this.questionId = questionId;
            this.error = error;
            type = PurchaseFormErrorCode.INVALID_ANSWER;
        }

        PurchaseFormError(PurchaseFormErrorCode type) {
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

        public PurchaseFormErrorCode getType() {
            return type;
        }
    }
}