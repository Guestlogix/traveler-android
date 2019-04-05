package com.guestlogix.traveleruikit.widgets;


import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.guestlogix.travelercorekit.models.*;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.forms.Form;
import com.guestlogix.traveleruikit.forms.descriptors.InputDescriptor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class QuestionsForm extends FrameLayout implements Form.DataSource {

    /**
     * Callback interface which is invoked whenever the {@link QuestionsForm} finished gathering answers for a {@link BookingForm}
     */
    public interface QuestionsFormSubmitted {
        /**
         * Callback to be invoked when the form has finished gathering answers.
         */
        void onQuestionFormSubmitted();
    }

    // Views
    private View view;
    private Form form;

    // Required Data for this widget to run
    private BookingForm bookingForm;
    private BookingForm.BookingFormError currentError;

    /**
     * Callback to be invoked whenever the questions form is completed.
     */
    private QuestionsFormSubmitted questionsFormSubmitted;

    public QuestionsForm(@NonNull Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public QuestionsForm(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public QuestionsForm(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public QuestionsForm(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        if (!isInEditMode()) {
            view = LayoutInflater.from(context).inflate(R.layout.widget_questions_form, this, true);

            form = view.findViewById(R.id.form_questionsWidget_container);
            form.setDataSource(this);
            form.setOnFormValueChangedListener(this::onFormValueChanged);
            form.setOnFormClickListener(this::onFormClick);
        }
    }

    public void setForm(BookingForm bookingForm) {
        this.bookingForm = bookingForm;
        form.reload();

    }

    @Override
    public int getSectionCount() {
        return bookingForm == null ? 0 : bookingForm.getQuestionGroups().size() + 1;
    }

    @Override
    public int getFieldCount(int sectionId) {
        if (bookingForm == null) {
            return 0;
        }

        // Submit
        if (sectionId == bookingForm.getQuestionGroups().size()) {
            return 1;
        }

        return bookingForm.getQuestionGroups().get(sectionId).getQuestions().size();
    }

    @Override
    public int getType(int sectionId, int fieldId) {
        if (bookingForm == null) {
            return 0;
        }

        // Submit
        if (sectionId == bookingForm.getQuestionGroups().size()) {
            return Form.FormType.BUTTON.getValue();
        }

        Question question = bookingForm.getQuestionGroups().get(sectionId).getQuestions().get(fieldId);

        switch (question.getType()) {
            case QUANTITY:
                return Form.FormType.QUANTITY.getValue();
            case STRING:
                return Form.FormType.TEXT.getValue();
            case MULTIPLE_CHOICE:
                return Form.FormType.SPINNER.getValue();
            case DATE:
                return Form.FormType.DATE.getValue();
        }

        return 0;
    }

    @NonNull
    @Override
    public InputDescriptor getInputDescriptor(int sectionId, int fieldId, int _type) {
        // Submit
        if (sectionId == bookingForm.getQuestionGroups().size()) {
            InputDescriptor i = new InputDescriptor();
            i.title = view.getContext().getString(R.string.book_now);
            return i;
        }

        Question question = bookingForm.getQuestionGroups().get(sectionId).getQuestions().get(fieldId);
        InputDescriptor descriptor = new InputDescriptor();

        descriptor.title = question.getTitle();
        descriptor.subtitle = question.getDescription();

        if (question.getType() == QuestionType.MULTIPLE_CHOICE) {
            List<String> optionValues = new ArrayList<>();
            List<Choice> questionChoices = (List<Choice>) question.getOptions();

            for (Choice c : questionChoices) {
                optionValues.add(c.getValue());
            }

            descriptor.options = optionValues;
        }

        return descriptor;
    }

    @Nullable
    @Override
    public String getTitle(int sectionId) {
        if (bookingForm == null) {
            return null;
        }

        // Submit
        if (sectionId == bookingForm.getQuestionGroups().size()) {
            return null;
        }

        return bookingForm.getQuestionGroups().get(sectionId).getTitle();
    }

    @Nullable
    @Override
    public String getDisclaimer(int sectionId) {
        if (bookingForm == null) {
            return null;
        }

        // Submit
        if (sectionId == bookingForm.getQuestionGroups().size()) {
            return null;
        }

        return bookingForm.getQuestionGroups().get(sectionId).getDisclaimer();
    }

    @Nullable
    @Override
    public Pair<String, Form.FormMessage> getMessage(int sectionId, int fieldId) {
        if (currentError != null && currentError.getGroupId() == sectionId && currentError.getQuestionId() == fieldId) {
            String message;
            switch (currentError.getError()) {
                case REGEX_MISMATCH:
                    message = view.getContext().getString(R.string.regex_mismatch);
                    break;
                case REQUIRED:
                    message = view.getContext().getString(R.string.required);
                    break;
                case BAD_QUANTITY:
                    message = view.getContext().getString(R.string.bad_quantity);
                    break;
                default:
                    message = "";
            }

            return new Pair<>(message, Form.FormMessage.ALERT);
        }

        return null;
    }

    @Nullable
    @Override
    public Object getValue(int sectionId, int fieldId) {
        if (bookingForm == null) {
            return null;
        }

        // Submit
        if (sectionId == bookingForm.getQuestionGroups().size()) {
            return null;
        }

        Question question = bookingForm.getQuestionGroups().get(sectionId).getQuestions().get(fieldId);
        Answer answer = bookingForm.getAnswer(question);

        if (answer == null) {
            return null;
        }

        switch (question.getType()) {
            case DATE:
                return ((DateAnswer) answer).getValue();
            case MULTIPLE_CHOICE:
                return ((MultipleChoiceSelection) answer).getValue();
            case STRING:
                return ((TextualAnswer) answer).getValue();
            case QUANTITY:
                return null;
        }

        return null;
    }

    /**
     * Subscribes to {@link QuestionsFormSubmitted} events.
     *
     * @param questionsFormSubmitted callback to be invoked.
     */
    public void setQuestionFormSubmittedListener(QuestionsFormSubmitted questionsFormSubmitted) {
        this.questionsFormSubmitted = questionsFormSubmitted;
    }

    private void onFormValueChanged(int sectionId, int fieldId, Object value) {
        Question question = bookingForm.getQuestionGroups().get(sectionId).getQuestions().get(fieldId);
        Answer a;
        switch (question.getType()) {
            case MULTIPLE_CHOICE:
                a = new MultipleChoiceSelection((Integer) value, question);
                break;
            case STRING:
                a = new TextualAnswer(value.toString(), question);
                break;
            case DATE:
                a = new DateAnswer((Calendar) value, question);
                break;
            case QUANTITY:
                a = new QuantityAnswer((Integer) value, question);
                break;
            default:
                a = null;
        }

        if (a != null) {
            bookingForm.addAnswer(a);
        }
    }

    private void onFormClick(int sectionId, int _fieldId) {
        if (bookingForm == null) {
            return;
        }

        if (sectionId == bookingForm.getQuestionGroups().size()) {
            List<BookingForm.BookingFormError> errors = bookingForm.validate();

            if (!errors.isEmpty()) {
                // Hide current error if its not null
                if (currentError != null) {
                    form.reload(currentError.getGroupId(), currentError.getQuestionId());
                }

                currentError = errors.get(0);
                form.reload(currentError.getGroupId(), currentError.getQuestionId());
            } else if (null != questionsFormSubmitted) {
                questionsFormSubmitted.onQuestionFormSubmitted(); // Notify activity form is done
            }
        }
    }
}
