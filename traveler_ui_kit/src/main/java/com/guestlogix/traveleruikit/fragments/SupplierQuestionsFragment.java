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
import com.guestlogix.travelercorekit.validators.ValidationError;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.forms.Form;
import com.guestlogix.traveleruikit.forms.descriptors.ButtonDescriptor;
import com.guestlogix.traveleruikit.forms.descriptors.InputDescriptor;
import com.guestlogix.traveleruikit.forms.utilities.FormType;
import com.guestlogix.viewmodels.BookingViewModel;

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
        viewModel.getBookingFormErrorsObservable().observe(this, this::updateForm);
        populateForm();

        return view;
    }

    private void populateForm() {
        form.setDataSource(new Form.DataSource() {
            int submitIndex = 0;

            @Override
            public int getSectionCount() {
                submitIndex = viewModel.getSectionCount();
                return submitIndex + 1;
            }

            @Override
            public int getFieldCount(int sectionId) {
                if (sectionId != submitIndex) {
                    return viewModel.getFieldCount(sectionId);
                } else {
                    return 1;
                }
            }

            @Override
            public int getType(int sectionId, int fieldId) {
                if (sectionId != submitIndex) {
                    return viewModel.getType(sectionId, fieldId);
                } else {
                    return FormType.BUTTON.getValue();
                }
            }

            @Override
            public String getTitle(int sectionId) {
                if (sectionId != submitIndex) {
                    return viewModel.getTitle(sectionId);
                } else {
                    return null;
                }
            }

            @Override
            public String getDisclaimer(int sectionId) {
                if (sectionId != submitIndex) {
                    return viewModel.getDisclaimer(sectionId);
                } else {
                    return null;
                }
            }

            @Override
            public InputDescriptor getDescriptor(int sectionId, int fieldId, int type) {
                if (sectionId != submitIndex) {
                    return viewModel.buildInputDescriptor(sectionId, fieldId);
                } else {
                    ButtonDescriptor b = new ButtonDescriptor();
                    b.text = getString(R.string.checkout);
                    return b;
                }
            }

            @Nullable
            @Override
            public String getError(int sectionId, int fieldId) {
                ValidationError error = viewModel.getFormError(sectionId, fieldId);

                if (error == null) {
                    return null;
                }

                switch (error) {
                    case REQUIRED:
                        return getString(R.string.required);
                    case REGEX_MISMATCH:
                        return getString(R.string.regex_mismatch);
                }

                return null;
            }

            @Nullable
            @Override
            public Object getValue(int sectionId, int fieldId) {
                return viewModel.getFormValue(sectionId, fieldId);
            }
        });

        form.setOnFormValueChangedListener(viewModel::addAnswer);
        form.setOnFormClickListener(((sectionId, fieldId) -> {
            if (sectionId == viewModel.getSectionCount()) {
                viewModel.submitBookingForm();
            }
        }));
    }

    private void updateForm(@NonNull Pair<Integer, Integer> scrollTo) {
        form.reload();
        form.scrollToPosition(scrollTo.first, scrollTo.second);
    }
}
