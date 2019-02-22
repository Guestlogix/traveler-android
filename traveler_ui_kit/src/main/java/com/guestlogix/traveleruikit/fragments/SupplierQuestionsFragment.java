package com.guestlogix.traveleruikit.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.guestlogix.travelercorekit.models.*;
import com.guestlogix.travelercorekit.validators.ValidationError;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.forms.Form;
import com.guestlogix.traveleruikit.forms.descriptors.ButtonDescriptor;
import com.guestlogix.traveleruikit.forms.descriptors.InputDescriptor;
import com.guestlogix.traveleruikit.forms.descriptors.SpinnerDescriptor;
import com.guestlogix.traveleruikit.forms.descriptors.TextDescriptor;
import com.guestlogix.traveleruikit.forms.utilities.FormType;
import com.guestlogix.traveleruikit.viewmodels.BookingViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment which displays all the supplier questions in the form.
 */
public class SupplierQuestionsFragment extends Fragment {
    private Form form;
    private BookingViewModel viewModel;

    public SupplierQuestionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_supplier_questions, container, false);

        form = view.findViewById(R.id.questionsForm);
        viewModel = ViewModelProviders.of(getActivity()).get(BookingViewModel.class);

        viewModel.getBookingFormErrorPosition().observe(getViewLifecycleOwner(), this::updateForm);
        viewModel.getBookingForm().observe(getViewLifecycleOwner(), this::buildSupplierForm);

        return view;
    }

    private void buildSupplierForm(BookingForm bookingForm) {
        createNewDataSource(bookingForm);
        setFormListeners(bookingForm);
    }

    private void createNewDataSource(BookingForm bookingForm) {
        List<QuestionGroup> questionGroups = bookingForm.getQuestionGroups();
        form.setDataSource(new Form.DataSource() {
            @Override
            public int getSectionCount() {
                return questionGroups.size() + 1; // +1 for submit button
            }

            @Override
            public int getFieldCount(int sectionId) {
                if (sectionId != questionGroups.size()) {
                    return questionGroups.get(sectionId).getQuestions().size();
                }

                return 1;
            }

            @Override
            public int getType(int sectionId, int fieldId) {
                if (sectionId != questionGroups.size()) {
                    Question q = questionGroups.get(sectionId).getQuestions().get(fieldId);
                    return determineQuestionType(q);
                }

                return FormType.BUTTON.getValue();
            }

            @Override
            public String getTitle(int sectionId) {
                if (sectionId != questionGroups.size()) {
                    return questionGroups.get(sectionId).getTitle();
                }

                return null;
            }

            @Override
            public String getDisclaimer(int sectionId) {
                if (sectionId != questionGroups.size()) {
                    return questionGroups.get(sectionId).getTitle();
                }

                return null;
            }

            @Override
            public InputDescriptor getDescriptor(int sectionId, int fieldId, int type) {
                if (sectionId != questionGroups.size()) {
                    Question q = questionGroups.get(sectionId).getQuestions().get(fieldId);
                    return buildInputDescriptors(q);
                }

                ButtonDescriptor b = new ButtonDescriptor();
                b.text = getString(R.string.checkout);

                return b;
            }

            @Nullable
            @Override
            public String getError(int sectionId, int fieldId) {
                if (sectionId != questionGroups.size()) {
                    Question q = questionGroups.get(sectionId).getQuestions().get(fieldId);
                    List<BookingForm.BookingFormError> errors = bookingForm.getErrors(q);

                    return errors == null || errors.isEmpty() ? null : translateErrorCodeToString(errors.get(0).error);
                }

                return null;
            }

            @Nullable
            @Override
            public Object getValue(int sectionId, int fieldId) {
                if (sectionId != questionGroups.size()) {
                    Question q = questionGroups.get(sectionId).getQuestions().get(fieldId);
                    Answer a = bookingForm.getAnswer(q);

                    return getAnswer(q, a);
                }

                return null;
            }
        });
    }

    private void setFormListeners(BookingForm bookingForm) {
        form.setOnFormClickListener((sectionId, fieldId) -> {
            if (sectionId == bookingForm.getQuestionGroups().size()) {
                // Submit button pressed.
                viewModel.submitQuestions();
            } else {
                // Some other field was pressed.
            }
        });

        form.setOnFormValueChangedListener(((sectionId, fieldId, value) -> {
            Question q = bookingForm.getQuestionGroups().get(sectionId).getQuestions().get(fieldId);
            viewModel.updateValueForQuestion(q, value);
        }));
    }

    // Translates question types into form types.
    private int determineQuestionType(Question question) {
        QuestionType type = question.getType();

        switch (type) {
            case MULTIPLE_CHOICE:
                return FormType.SPINNER.getValue();
            case STRING:
                return FormType.TEXT.getValue();
            default:
                return 0;
        }
    }

    // Builds input descriptors based on the type of question.
    private InputDescriptor buildInputDescriptors(Question question) {
        QuestionType type = question.getType();

        switch (type) {
            case STRING:
                return buildTextDescriptor(question);
            case MULTIPLE_CHOICE:
                return buildSpinnerDescriptor(question);
            default:
                return null;
        }
    }

    private TextDescriptor buildTextDescriptor(Question question) {
        TextDescriptor t = new TextDescriptor();

        t.hint = question.getTitle();

        return t;
    }

    private SpinnerDescriptor buildSpinnerDescriptor(Question question) {
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
    }

    private String translateErrorCodeToString(ValidationError error) {
        switch (error) {
            case REGEX_MISMATCH:
                return getString(R.string.regex_mismatch);
            case REQUIRED:
                return getString(R.string.required);
            default:
                return "";
        }
    }

    private Object getAnswer(Question q, Answer a) {
        QuestionType type = q.getType();

        if (a != null) {
            switch (type) {
                case MULTIPLE_CHOICE:
                    MultipleChoiceSelection m = (MultipleChoiceSelection) a;
                    return m.getValue();
                case STRING:
                    TextualAnswer t = (TextualAnswer) a;
                    return t.getValue();
                default:
                    return null;
            }
        } else {
            return null;
        }
    }

    private void updateForm(@NonNull BookingViewModel.Event<Pair<Integer, Integer>> event) {
        Pair<Integer, Integer> update = event.getData();

        if (update != null) {
            Integer sectionId = update.first;
            Integer fieldId = update.second;

            if (null != sectionId && null != fieldId) {
                form.updateField(sectionId, fieldId);
                form.scrollToPosition(sectionId, fieldId);
            }
        }
    }
}
