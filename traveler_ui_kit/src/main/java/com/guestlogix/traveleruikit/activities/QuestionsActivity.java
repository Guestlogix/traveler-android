package com.guestlogix.traveleruikit.activities;

import android.os.Bundle;
import android.util.Pair;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import com.guestlogix.travelercorekit.TravelerLog;
import com.guestlogix.travelercorekit.models.*;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.forms.Form;
import com.guestlogix.traveleruikit.forms.descriptors.ButtonDescriptor;
import com.guestlogix.traveleruikit.forms.descriptors.InputDescriptor;
import com.guestlogix.traveleruikit.forms.descriptors.SpinnerDescriptor;
import com.guestlogix.traveleruikit.forms.descriptors.TextDescriptor;
import com.guestlogix.traveleruikit.viewmodels.BookingQuestionsViewModel;

import java.util.ArrayList;
import java.util.List;

public class QuestionsActivity extends AppCompatActivity {
    public static final String EXTRA_BOOKING_FORM = "EXTRA_QUESTIONS_ACTIVITY_BOOKING_FORM";

    private BookingQuestionsViewModel viewModel;

    // Views
    private Form form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        Bundle extras = getIntent().getExtras();

        if (extras != null && extras.containsKey(EXTRA_BOOKING_FORM)) {
            BookingForm bookingForm = (BookingForm) extras.getSerializable(EXTRA_BOOKING_FORM);

            if (bookingForm != null) {
                viewModel = ViewModelProviders.of(this).get(BookingQuestionsViewModel.class);
                viewModel.setup(bookingForm);
                form = findViewById(R.id.form_questionsActivity);

                viewModel.getBookingForm().observe(this, this::onBookingForm);
                viewModel.getError().observe(this, error -> form.reload(error.getGroupId(), error.getQuestionId()));

                return;
            }
        }

        TravelerLog.e("QuestionsActivity requires a BookingForm to operate.");
        finish();
    }

    // So we don't crowd on create
    private void onBookingForm(BookingForm bookingForm) {
        form.setDataSource(new Form.DataSource() {
            @Override
            public int getSectionCount() {
                return bookingForm.getQuestionGroups().size() + 1; // Submit btn
            }

            @Override
            public int getFieldCount(int sectionId) {
                if (sectionId == bookingForm.getQuestionGroups().size()) {
                    return 1; // Submit btn
                }

                return bookingForm.getQuestionGroups().get(sectionId).getQuestions().size();
            }

            @Override
            public int getType(int sectionId, int fieldId) {
                if (sectionId == bookingForm.getQuestionGroups().size()) {
                    return Form.FormType.BUTTON.getValue(); // Submit btn
                }

                Question question = bookingForm.getQuestionGroups().get(sectionId).getQuestions().get(fieldId);
                QuestionType type = question.getType();

                switch (type) {
                    case MULTIPLE_CHOICE:
                        return Form.FormType.SPINNER.getValue();
                    case STRING:
                        return Form.FormType.TEXT.getValue();
                    default:
                        return 0;
                }
            }

            @NonNull
            @Override
            public InputDescriptor getInputDescriptor(int sectionId, int fieldId, int type) {
                if (sectionId == bookingForm.getQuestionGroups().size()) {
                    ButtonDescriptor b = new ButtonDescriptor();
                    b.text = getString(R.string.book_now);
                    return b;
                }

                Question question = bookingForm.getQuestionGroups().get(sectionId).getQuestions().get(fieldId);
                QuestionType qType = question.getType();
                switch (qType) {
                    case STRING:
                        TextDescriptor t = new TextDescriptor();
                        t.hint = question.getTitle();
                        return t;

                    case MULTIPLE_CHOICE:
                        SpinnerDescriptor s = new SpinnerDescriptor();

                        if (question.getOptions() != null) {
                            List<Choice> choices = (List<Choice>) question.getOptions(); // Expecting the options in MC type to be list.
                            List<String> options = new ArrayList<>();

                            if (choices != null) {
                                for (Choice c : choices) {
                                    options.add(c.getValue());
                                }
                            }

                            s.options = options;
                        }

                        s.title = question.getTitle();
                        s.subtitle = question.getDescription();
                        return s;

                    default:
                        return null;
                }
            }

            @Nullable
            @Override
            public String getTitle(int sectionId) {
                return bookingForm.getQuestionGroups().get(sectionId).getTitle();
            }

            @Nullable
            @Override
            public String getDisclaimer(int sectionId) {
                return bookingForm.getQuestionGroups().get(sectionId).getDisclaimer();
            }

            @Nullable
            @Override
            public Pair<String, Form.FormMessage> getMessage(int sectionId, int fieldId) {
                BookingForm.BookingFormError focusedError = viewModel.getMessage(sectionId, fieldId);
                // For now only display error messages.
                if (focusedError != null && sectionId == focusedError.getGroupId() && fieldId == focusedError.getQuestionId()) {
                    String message = null;

                    switch (focusedError.getError()) {
                        case REQUIRED:
                            message = getString(R.string.required);
                            break;
                        case REGEX_MISMATCH:
                            message = getString(R.string.regex_mismatch);
                            break;
                    }

                    return new Pair<>(message, Form.FormMessage.ALERT);
                }

                return null;
            }

            @Nullable
            @Override
            public Object getValue(int sectionId, int fieldId) {
                if (sectionId == bookingForm.getQuestionGroups().size()) {
                    return null; // Submit btn
                }

                Question question = bookingForm.getQuestionGroups().get(sectionId).getQuestions().get(fieldId);
                QuestionType type = question.getType();
                Answer answer = bookingForm.getAnswer(question);

                switch (type) {
                    case STRING:
                        TextualAnswer textualAnswer = (TextualAnswer) answer;
                        return textualAnswer == null ? null : textualAnswer.getValue();
                    case MULTIPLE_CHOICE:
                        MultipleChoiceSelection multipleChoiceSelection = (MultipleChoiceSelection) answer;
                        return multipleChoiceSelection == null ? null : multipleChoiceSelection.getValue();
                    default:
                        return null;
                }
            }
        });

        form.setOnFormValueChangedListener((sectionId, fieldId, value) -> {
            Question question = bookingForm.getQuestionGroups().get(sectionId).getQuestions().get(fieldId);
            QuestionType type = question.getType();
            Answer answer = null;
            switch (type) {
                case MULTIPLE_CHOICE:
                    answer = new MultipleChoiceSelection((Integer) value, question);
                    break;
                case STRING:
                    answer = new TextualAnswer((String) value, question);
                    break;
            }

            if (answer != null) {
                bookingForm.addAnswer(answer);
            }

        });

        form.setOnFormClickListener((sectionId, fieldId) -> {
            if (sectionId == bookingForm.getQuestionGroups().size()) {
                viewModel.submit();
            }
        });
    }
}
