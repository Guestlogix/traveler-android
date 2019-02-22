package com.guestlogix.travelercorekit.models;

import android.annotation.SuppressLint;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import com.guestlogix.travelercorekit.validators.PatternValidationRule;
import com.guestlogix.travelercorekit.validators.RequiredValidationRule;
import com.guestlogix.travelercorekit.validators.ValidationError;
import com.guestlogix.travelercorekit.validators.ValidationRule;

import java.io.Serializable;
import java.util.*;


public class BookingForm implements Serializable {
    private List<QuestionGroup> questionGroups;
    private Map<IndexedQuestion, Answer> answerMap;
    transient private Map<IndexedQuestion, List<BookingFormError>> errorMap;
    transient private Set<IndexedQuestion> flatQuestions;
    private List<Pass> passes;

    public BookingForm(List<Pass> passes) {
        questionGroups = new ArrayList<>();
        answerMap = new HashMap<>();
        flatQuestions = new HashSet<>();

        // Required questions.
        questionGroups.add(buildDefault());

        // Copy questions for each pass and assign other id.
        for (int i = 0; i < passes.size(); i++) {
            Pass p = passes.get(i);
            questionGroups.add(buildIndexedQuestionGroup(p, i));
        }

        this.passes = passes;
    }

    public void addAnswer(Answer answer, Question question) {
        if (question instanceof IndexedQuestion) {
            IndexedQuestion q = (IndexedQuestion) question;

            if (flatQuestions.contains(q)) {
                answerMap.put(q, answer);
                return;
            }
        }
        throw new IllegalArgumentException("Question is not part of the BookingForm");
    }

    @Nullable
    public Answer getAnswer(Question question) {
        if (question instanceof IndexedQuestion) {
            IndexedQuestion q = (IndexedQuestion) question;

            if (flatQuestions.contains(q)) {
                return answerMap.get(q);
            }
        }
        throw new IllegalArgumentException("Question is not part of the BookingForm");
    }

    public List<QuestionGroup> getQuestionGroups() {
        return questionGroups;
    }

    public List<Pass> getPasses() {
        return passes;
    }

    @Nullable
    public Pair<Integer, Integer> validate() {
        errorMap = new HashMap<>();
        Pair<Integer, Integer> firstError = null;

        for (int i = 0; i < questionGroups.size(); i++) {
            for (int j = 0; j < questionGroups.get(i).getQuestions().size(); j++) {
                IndexedQuestion question = (IndexedQuestion) questionGroups.get(i).getQuestions().get(j);
                Answer answer = getAnswer(question);

                if (question.getValidationRules() != null) {
                    for (ValidationRule validationRule : question.getValidationRules()) {
                        boolean isValid = validationRule.validate(answer);

                        if (!isValid) {
                            BookingFormError error = new BookingFormError(i, j, validationRule.getError());

                            if (firstError == null) {
                                firstError = new Pair<>(i, j);
                            }

                            if (!errorMap.containsKey(question)) {
                                errorMap.put(question, new ArrayList<>());
                            }

                            errorMap.get(question).add(error);
                        }
                    }
                }
            }
        }

        return firstError;
    }

    @Nullable
    public List<BookingFormError> getErrors(Question question) {
        if (question instanceof IndexedQuestion) {
            IndexedQuestion q = (IndexedQuestion) question;

            if (errorMap == null) {
                return null;
            } else {
                return errorMap.get(q);
            }
        }

        throw new IllegalArgumentException("Question is not part of the form.");
    }

    @SuppressLint("DefaultLocale")
    private QuestionGroup buildIndexedQuestionGroup(Pass pass, int index) {
        List<Question> groupQuestions = makeQuestions(pass, index);
        String title = String.format("Guest %d - %s", index + 1, pass.getName());

        return new QuestionGroup(title, null, groupQuestions);
    }

    // TODO: Extract default string literals into a resource.
    private QuestionGroup buildDefault() {
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

        return new QuestionGroup("Primary Contact", "The primary contact is the person that will receive purchase confirmation and passes. This person does not have to be part of the group", primaryQuestions);
    }

    @SuppressLint("DefaultLocale")
    private List<Question> makeQuestions(Pass p, int index) {
        List<Question> questions = new ArrayList<>();

        for (Question q : p.getQuestions()) {
            Question copy = createQuestion(q.getId(), q.getTitle(), q.getDescription(), q.getType(), q.getValidationRules(), q.getOptions(), index);

            questions.add(copy);
        }

        return questions;
    }

    private Question createQuestion(String id, String title, String description, QuestionType type, List<ValidationRule> rules, Object o, Integer i) {
        IndexedQuestion indexedQuestion = new IndexedQuestion(id, title, description, type, rules, o, i);
        flatQuestions.add(indexedQuestion);
        return indexedQuestion;
    }

    public class BookingFormError {
        final int groupIndex;
        final int questionIndex;
        public final ValidationError error;

        BookingFormError(int groupIndex, int questionIndex, ValidationError error) {
            this.groupIndex = groupIndex;
            this.questionIndex = questionIndex;
            this.error = error;
        }
    }

    private class IndexedQuestion extends Question implements Serializable {
        int index;

        IndexedQuestion(String id, String title, String description, QuestionType type, List<ValidationRule> rules,
                        Object options, int index) {
            super(id, title, description, type, rules, options);

            this.index = index;
        }

        // Helper method for better mapping.
        @Override
        public int hashCode() {
            return super.hashCode() * index * 59;
        }
    }
}
