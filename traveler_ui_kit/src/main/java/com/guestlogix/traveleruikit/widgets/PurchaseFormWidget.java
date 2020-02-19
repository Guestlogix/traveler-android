package com.guestlogix.traveleruikit.widgets;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.guestlogix.travelercorekit.models.Answer;
import com.guestlogix.travelercorekit.models.Choice;
import com.guestlogix.travelercorekit.models.DateAnswer;
import com.guestlogix.travelercorekit.models.MultipleChoiceSelection;
import com.guestlogix.travelercorekit.models.PatternValidationRule;
import com.guestlogix.travelercorekit.models.PurchaseForm;
import com.guestlogix.travelercorekit.models.QuantityAnswer;
import com.guestlogix.travelercorekit.models.Question;
import com.guestlogix.travelercorekit.models.QuestionGroup;
import com.guestlogix.travelercorekit.models.QuestionType;
import com.guestlogix.travelercorekit.models.RequiredValidationRule;
import com.guestlogix.travelercorekit.models.TextualAnswer;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.forms.Form;
import com.guestlogix.traveleruikit.forms.FormFieldType;
import com.guestlogix.traveleruikit.forms.FormHeader;
import com.guestlogix.traveleruikit.forms.FormMessage;
import com.guestlogix.traveleruikit.forms.models.ButtonFormModel;
import com.guestlogix.traveleruikit.forms.models.DateFormModel;
import com.guestlogix.traveleruikit.forms.models.FormModel;
import com.guestlogix.traveleruikit.forms.models.QuantityFormModel;
import com.guestlogix.traveleruikit.forms.models.SpinnerFormModel;
import com.guestlogix.traveleruikit.forms.models.TextFormModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Wrapper widget around the {@link Form} library. Used to display questions in a booking form.
 * <p>
 * Implement {@link FormCompletedListener} to get notified whenever the form is successfully built.
 */
public class PurchaseFormWidget extends Form implements
        Form.DataSource,
        Form.FormValueChangedListener,
        Form.FormClickListener {

    // Data
    private PurchaseForm purchaseForm;
    private List<PurchaseForm.PurchaseFormInputError> lstCurrentErrors;

    /**
     * Listener used to dispatch PurchaseForm completion events.
     */
    private FormCompletedListener formCompletedListener;

    public PurchaseFormWidget(@NonNull Context context) {
        super(context);
    }

    public PurchaseFormWidget(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PurchaseFormWidget(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setPurchaseForm(@NonNull PurchaseForm purchaseForm) {
        this.purchaseForm = purchaseForm;
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
        return purchaseForm.getQuestionGroups().size() + 1;
    }

    @Override
    public int getFieldCount(int sectionId) {
        if (sectionId == purchaseForm.getQuestionGroups().size()) {
            return 1;
        }

        return purchaseForm.getQuestionGroups().get(sectionId).getQuestions().size();
    }

    @NonNull
    @Override
    public FormModel getModel(int sectionId, int fieldId) {
        if (sectionId == purchaseForm.getQuestionGroups().size()) {
            return new ButtonFormModel(getContext().getString(R.string.next));
        }

        Question question = purchaseForm.getQuestionGroups().get(sectionId).getQuestions().get(fieldId);

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
        if (sectionId == purchaseForm.getQuestionGroups().size()) {
            return FormFieldType.BUTTON;
        }

        Question q = purchaseForm.getQuestionGroups().get(sectionId).getQuestions().get(fieldId);

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
        if (sectionId == purchaseForm.getQuestionGroups().size()) {
            return null;
        }

        QuestionGroup questionGroup = purchaseForm.getQuestionGroups().get(sectionId);

        if (questionGroup.getTitle() != null || questionGroup.getDisclaimer() != null) {
            return new FormHeader(questionGroup.getTitle(), questionGroup.getDisclaimer());
        }

        return null;
    }

    @Nullable
    @Override
    public FormMessage getMessage(int sectionId, int fieldId) {
        if (lstCurrentErrors == null || lstCurrentErrors.isEmpty()) {
            return null;
        }


        for (int i = 0; i < lstCurrentErrors.size(); i++) {
            PurchaseForm.PurchaseFormInputError currentError = lstCurrentErrors.get(i);
            if (currentError.getGroupId() == sectionId && currentError.getQuestionId() == fieldId) {
                String message;

                if (currentError.getFailedValidationRule() instanceof RequiredValidationRule) {
                    message = getContext().getString(R.string.required);
                } else if (currentError.getFailedValidationRule() instanceof PatternValidationRule) {
                    message = currentError.getFailedValidationRule().getErrorMessage();
                } else {
                    message = "";
                }

                return new FormMessage(message, FormMessage.FormMessageType.ALERT);
            }
        }

        return null;
    }

    @Nullable
    @Override
    public Object getValue(int sectionId, int fieldId) {
        if (sectionId == purchaseForm.getQuestionGroups().size()) {
            return null;
        }

        Question question = purchaseForm.getQuestionGroups().get(sectionId).getQuestions().get(fieldId);
        Answer answer = purchaseForm.getAnswer(question);

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
        if (sectionId == purchaseForm.getQuestionGroups().size()) {
            return;
        }

        Question question = purchaseForm.getQuestionGroups().get(sectionId).getQuestions().get(fieldId);
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

        purchaseForm.addAnswer(answer);
    }

    @Override
    public void onFormClick(int sectionId, int fieldId) {
        if (purchaseForm == null) {
            return;
        }

        if (sectionId == purchaseForm.getQuestionGroups().size()) {
            lstCurrentErrors = purchaseForm.validate();
            reloadAllNonHeaders();

            if (lstCurrentErrors.isEmpty()) {
                if (null != formCompletedListener) {
                    lstCurrentErrors = null;
                    formCompletedListener.onFormCompleted(purchaseForm); // Notify activity form is done
                }
            } else {
                //scroll to first error
                smoothScrollToPosition(lstCurrentErrors.get(0).getGroupId(), lstCurrentErrors.get(0).getQuestionId());
            }
        }
    }

    /**
     * Interface definition for a callback to be invoked whenever the {@link PurchaseForm} is ready to submit.
     */
    public interface FormCompletedListener {
        /**
         * Called whenever the {@link PurchaseForm} is ready to submit.
         *
         * @param purchaseForm Filled PurchaseForm
         */
        void onFormCompleted(PurchaseForm purchaseForm);
    }
}
