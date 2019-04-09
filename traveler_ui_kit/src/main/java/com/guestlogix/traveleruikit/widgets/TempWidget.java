package com.guestlogix.traveleruikit.widgets;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.guestlogix.travelercorekit.models.*;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.forms.FormHeader;
import com.guestlogix.traveleruikit.forms.FormMessage;
import com.guestlogix.traveleruikit.forms.Temp;
import com.guestlogix.traveleruikit.forms.models.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TempWidget extends Temp implements
        Temp.DataSource,
        Temp.FormValueChangedListener,
        Temp.FormClickListener {

    // Data
    private BookingForm bookingForm;
    private BookingForm.BookingFormError currentError;
    private FormCompletedListener formCompletedListener;

    public TempWidget(@NonNull Context context) {
        super(context);
    }

    public TempWidget(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TempWidget(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setBookingForm(@NonNull BookingForm bookingForm) {
        this.bookingForm = bookingForm;
        super.setDataSource(this);
    }

    public void setFormCompletedListener(FormCompletedListener formCompletedListener) {
        this.formCompletedListener = formCompletedListener;
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

        switch (question.getType()) {
            case QUANTITY:
                return new QuantityFormModel(question.getTitle(), question.getDescription(), 10, 0);
            case DATE:
                return new DateFormModel(question.getTitle(), null, null);
            case MULTIPLE_CHOICE:
                List<String> options = new ArrayList<>();

                for (Choice c : (List<Choice>) question.getOptions()) {
                    options.add(c.getValue());
                }

                return new SpinnerFormModel(question.getTitle(), options);
            case STRING:
            default:
                return new TextFormModel(question.getTitle());
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

        switch (question.getType()) {
            case DATE:
                return ((DateAnswer) answer).getValue();
            case MULTIPLE_CHOICE:
                return ((MultipleChoiceSelection) answer).getValue();
            case QUANTITY:
                return ((QuantityAnswer) answer).getValue();
            case STRING:
                return ((TextualAnswer) answer).getValue();
        }

        return null;
    }

    @Override
    public void onFormValueChanged(int sectionId, int fieldId, Object value) {
        if (sectionId == bookingForm.getQuestionGroups().size()) {
            return;
        }

        Question question = bookingForm.getQuestionGroups().get(sectionId).getQuestions().get(fieldId);
        Answer answer = null;

        switch (question.getType()) {
            case DATE:
                answer = new DateAnswer((Calendar) value, question);
                break;
            case MULTIPLE_CHOICE:
                answer = new MultipleChoiceSelection((Integer) value, question);
                break;
            case QUANTITY:
                answer = new QuantityAnswer((Integer) value, question);
                break;
            case STRING:
                answer = new TextualAnswer(value.toString(), question);
                break;
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
            } else if (null != formCompletedListener) {
                formCompletedListener.onFormCompleted(bookingForm); // Notify activity form is done
            }
        }
    }

    public interface FormCompletedListener {
        void onFormCompleted(BookingForm bookingForm);
    }
}
