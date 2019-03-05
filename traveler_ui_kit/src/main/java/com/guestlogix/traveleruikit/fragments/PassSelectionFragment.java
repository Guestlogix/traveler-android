package com.guestlogix.traveleruikit.fragments;


import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import com.guestlogix.travelercorekit.models.Pass;
import com.guestlogix.travelercorekit.models.Price;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.forms.Form;
import com.guestlogix.traveleruikit.forms.descriptors.InputDescriptor;
import com.guestlogix.traveleruikit.forms.descriptors.QuantityDescriptor;
import com.guestlogix.traveleruikit.viewmodels.BookingViewModel;
import com.guestlogix.traveleruikit.widgets.ActionStrip;

import java.util.List;

/**
 * Fragment which lets the user to select any number of passes.
 */
public class PassSelectionFragment extends BaseFragment {

    private Form form;
    private ActionStrip actionLayout;

    private BookingViewModel viewModel;

    public PassSelectionFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pass_selection, container, false);

        form = view.findViewById(R.id.passForm);
        actionLayout = view.findViewById(R.id.actionLayout);

        // Events.
        actionLayout.setActionOnClickListener(this::onBookNowClick);
        actionLayout.setButtonText(getString(R.string.book_now));
        actionLayout.setLabel(getString(R.string.label_starting_at));

        // View Model
        viewModel = ViewModelProviders.of(getActivityContext()).get(BookingViewModel.class);

        viewModel.getObservablePasses().observe(getViewLifecycleOwner(), this::buildPassForm);
        viewModel.getObservablePrice().observe(getViewLifecycleOwner(), this::onPriceChange);

        return view;
    }

    private void onBookNowClick(View view) {
        viewModel.submitPasses();
    }

    private void buildPassForm(List<Pass> passes) {
        createNewDataSource(passes);
        setFormListeners(passes);
    }

    private void createNewDataSource(List<Pass> passes) {
        form.setDataSource(new Form.DataSource() {
            @Override
            public int getSectionCount() {
                return 1;
            }

            @Override
            public int getFieldCount(int sectionId) {
                return passes.size();
            }

            @Override
            public int getType(int sectionId, int fieldId) {
                return Form.FormType.QUANTITY.getValue(); // Only dealing with quantities for passes.
            }

            @Override
            public String getTitle(int sectionId) {
                return null;
            }

            @Override
            public String getDisclaimer(int sectionId) {
                return null;
            }

            @Nullable
            @Override
            public Pair<String, Form.FormMessage> getMessage(int sectionId, int fieldId) {
                return null;
            }

            @NonNull
            @Override
            public InputDescriptor getInputDescriptor(int sectionId, int fieldId, int type) {
                Pass p = passes.get(fieldId);
                QuantityDescriptor q = new QuantityDescriptor();

                q.maxQuantity = p.getMaxQuantity();
                q.minQuantity = 0;
                q.title = p.getName();
                q.subtitle = p.getDescription();
                return q;
            }

            @Override
            public Object getValue(int sectionId, int fieldId) {
                return viewModel.getPassQuantity(passes.get(fieldId));
            }
        });
    }

    private void setFormListeners(List<Pass> passes) {
        form.setOnFormValueChangedListener(((sectionId, fieldId, value) -> {
            Pass p = passes.get(fieldId);
            viewModel.updateValueForPass(p, (Integer) value);
        }));
    }

    private void onPriceChange(Price price) {
        actionLayout.setValue(price.getFormattedValue());
    }
}
