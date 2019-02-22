package com.guestlogix.traveleruikit.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.guestlogix.travelercorekit.models.Pass;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.forms.Form;
import com.guestlogix.traveleruikit.forms.descriptors.InputDescriptor;
import com.guestlogix.traveleruikit.forms.descriptors.QuantityDescriptor;
import com.guestlogix.traveleruikit.forms.utilities.FormType;
import com.guestlogix.traveleruikit.viewmodels.BookingViewModel;

import java.util.List;

/**
 * Fragment which lets the user to select any number of passes.
 */
public class PassSelectionFragment extends Fragment {

    private Form form;
    private TextView priceLbl;
    private Button bookNowBtn;

    private BookingViewModel viewModel;

    public PassSelectionFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pass_selection, container, false);

        form = view.findViewById(R.id.passForm);
        priceLbl = view.findViewById(R.id.startingAtValueTextView);
        bookNowBtn = view.findViewById(R.id.bookNowBtn);

        // Events.
        bookNowBtn.setOnClickListener(this::onBookNowClick);

        // View Model
        viewModel = ViewModelProviders.of(getActivity()).get(BookingViewModel.class);

        viewModel.getPasses().observe(getViewLifecycleOwner(), this::buildPassForm);
        viewModel.getPrice().observe(getViewLifecycleOwner(), this::onPriceChange);

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
                return form.getType(FormType.QUANTITY); // Only dealing with quantities for passes.
            }

            @Override
            public String getTitle(int sectionId) {
                return null;
            }

            @Override
            public String getDisclaimer(int sectionId) {
                return null;
            }

            @Override
            public InputDescriptor getDescriptor(int sectionId, int fieldId, int type) {
                Pass p = passes.get(fieldId);
                QuantityDescriptor q = new QuantityDescriptor();

                q.maxQuantity = p.getMaxQuantity();
                q.minQuantity = 0;
                q.title = p.getName();
                q.subtitle = p.getDescription();
                return q;
            }

            @Nullable
            @Override
            public String getError(int sectionId, int fieldId) {
                return null;
            }

            @Nullable
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

    @SuppressLint("DefaultLocale")
    private void onPriceChange(Double price) {
        priceLbl.setText(String.format("$%.2f", price));
    }
}
