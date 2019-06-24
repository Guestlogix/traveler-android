package com.guestlogix.traveleruikit.widgets;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.guestlogix.travelercorekit.models.*;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.forms.FormFieldType;
import com.guestlogix.traveleruikit.forms.FormHeader;
import com.guestlogix.traveleruikit.forms.FormMessage;
import com.guestlogix.traveleruikit.forms.Form;
import com.guestlogix.traveleruikit.forms.models.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Wrapper widget around the {@link Form} library. Used to display questions in a booking form.
 * <p>
 * Implement {@link FormCompletedListener} to get notified whenever the form is successfully built.
 */
public class BookingFormWidget extends Form implements
        Form.DataSource,
        Form.FormValueChangedListener,
        Form.FormClickListener {

    // Data
    private BookingForm bookingForm;
    private BookingForm.BookingFormError currentError;

    /**
     * Listener used to dispatch BookingForm completion events.
     */
    private FormCompletedListener formCompletedListener;

    public BookingFormWidget(@NonNull Context context) {
        super(context);
    }

    public BookingFormWidget(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BookingFormWidget(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setBookingForm(@NonNull BookingForm bookingForm) {
        this.bookingForm = bookingForm;
        super.setDataSource(this);
        super.setFormValueChangedListener(this);
        super.setFormClickListener(this);
    }

    /**
     * Registers a callback to be invoked whenever the booking form widget detects that a form is ready to be submitted.
     *
     * @param l The callback that will run
     */
    public void setFormCompletedListener(FormCompletedListener l) {
        this.formCompletedListener = l;
    }

    @Override
    public int getSectionCount() {
        return bookingForm.getQuestionGroups().size() + 1;
    }

    @Override
    public int getFieldCount(int sectionId) {
        if (sectionId == bookingForm.getQuestionGroups().size()) {
            return 1;
        }

        return bookingForm.getQuestionGroups().get(sectionId).getQuestions().size();
    }

    @NonNull
    @Override
    public FormModel getModel(int sectionId, int fieldId) {
        if (sectionId == bookingForm.getQuestionGroups().size()) {
            return new ButtonFormModel(getContext().getString(R.string.next));
        }

        Question question = bookingForm.getQuestionGroups().get(sectionId).getQuestions().get(fieldId);

        if (question.getType() instanceof QuestionType.Quantity) {
            return new QuantityFormModel(question.getTitle(), question.getDescription(), 10, 0);
        } else if (question.getType() instanceof QuestionType.Date) {
            return new DateFormModel(question.getTitle(), null, null);
        } else if (question.getType() instanceof QuestionType.MultipleChoice) {
            QuestionType.MultipleChoice type = (QuestionType.MultipleChoice) question.getType();

            List<String> options = new ArrayList<>();

            for (Choice c : type.getChoices()) {
                options.add(c.getValue());
            }

            return new SpinnerFormModel(question.getTitle(), options);
        } else {
            return new TextFormModel(question.getTitle());
        }
    }

    @Override
    public FormFieldType getFieldType(int sectionId, int fieldId) {
        if (sectionId == bookingForm.getQuestionGroups().size()) {
            return FormFieldType.BUTTON;
        }

        Question q = bookingForm.getQuestionGroups().get(sectionId).getQuestions().get(fieldId);

        if (q.getType() instanceof QuestionType.Quantity) {
            return FormFieldType.QUANTITY;
        } else if (q.getType() instanceof QuestionType.Date) {
            return FormFieldType.DATE;
        } else if (q.getType() instanceof QuestionType.MultipleChoice) {
            return FormFieldType.SPINNER;
        } else {
            return FormFieldType.TEXT;
        }
    }

    @Nullable
    @Override
    public FormHeader getSectionHeader(int sectionId) {
        if (sectionId == bookingForm.getQuestionGroups().size()) {
            return null;
        }

        QuestionGroup questionGroup = bookingForm.getQuestionGroups().get(sectionId);

        if (questionGroup.getTitle() != null || questionGroup.getDisclaimer() != null) {
            return new FormHeader(questionGroup.getTitle(), questionGroup.getDisclaimer());
        }

        return null;
    }

    @Nullable
    @Override
    public FormMessage getMessage(int sectionId, int fieldId) {
        if (currentError != null && currentError.getGroupId() == sectionId && currentError.getQuestionId() == fieldId) {
            String message;

            switch (currentError.getError()) {
                case REGEX_MISMATCH:
                    message = getContext().getString(R.string.regex_mismatch);
                    break;
                case REQUIRED:
                    message = getContext().getString(R.string.required);
                    break;
                case BAD_QUANTITY:
                    message = getContext().getString(R.string.bad_quantity);
                    break;
                default:
                    message = "";
                    break;
            }

            return new FormMessage(message, FormMessage.FormMessageType.ALERT);
        }

        return null;
    }

    @Nullable
    @Override
    public Object getValue(int sectionId, int fieldId) {
        if (sectionId == bookingForm.getQuestionGroups().size()) {
            return null;
        }

        Question question = bookingForm.getQuestionGroups().get(sectionId).getQuestions().get(fieldId);
        Answer answer = bookingForm.getAnswer(question);

        if (answer == null) {
            return null;
        }

        if (answer instanceof DateAnswer) {
            return ((DateAnswer) answer).getValue();
        } else if (answer instanceof MultipleChoiceSelection) {
            return ((MultipleChoiceSelection) answer).getValue();
        } else if (answer instanceof QuantityAnswer) {
            return ((QuantityAnswer) answer).getValue();
        } else {
            return answer.getCodedValue();
        }
    }

    @Override
    public void onFormValueChanged(int sectionId, int fieldId, Object value) {
        if (sectionId == bookingForm.getQuestionGroups().size()) {
            return;
        }

        Question question = bookingForm.getQuestionGroups().get(sectionId).getQuestions().get(fieldId);
        Answer answer = null;

        if (question.getType() instanceof QuestionType.Date) {
            answer = new DateAnswer((Date) value, question);
        } else if (question.getType() instanceof QuestionType.MultipleChoice) {
            answer = new MultipleChoiceSelection((Integer) value, question);
        } else if (question.getType() instanceof QuestionType.Quantity) {
            answer = new QuantityAnswer((Integer) value, question);
        } else if (question.getType() instanceof QuestionType.Textual) {
            answer = new TextualAnswer(value.toString(), question);
        }

        bookingForm.addAnswer(answer);
    }

    @Override
    public void onFormClick(int sectionId, int fieldId) {
        if (bookingForm == null) {
            return;
        }

        if (sectionId == bookingForm.getQuestionGroups().size()) {
            List<BookingForm.BookingFormError> errors = bookingForm.validate();

            if (!errors.isEmpty()) {
                // Hide current error if its not null
                if (currentError != null) {
                    reload(currentError.getGroupId(), currentError.getQuestionId());
                }

                currentError = errors.get(0);
                reload(currentError.getGroupId(), currentError.getQuestionId());
                smoothScrollToPosition(currentError.getGroupId(), currentError.getQuestionId());
            } else if (null != formCompletedListener) {
                // Hide errors if any
                if (currentError != null) {
                    reload(currentError.getGroupId(), currentError.getQuestionId());
                    currentError = null;
                }

                formCompletedListener.onFormCompleted(bookingForm); // Notify activity form is done
            }
        }
    }

    /**
     * Interface definition for a callback to be invoked whenever the {@link BookingForm} is ready to submit.
     */
    public interface FormCompletedListener {
        /**
         * Called whenever the {@link BookingForm} is ready to submit.
         *
         * @param bookingForm Filled BookingForm
         */
        void onFormCompleted(BookingForm bookingForm);
    }
}
