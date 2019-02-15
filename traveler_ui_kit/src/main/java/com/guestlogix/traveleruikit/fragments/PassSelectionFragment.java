package com.guestlogix.traveleruikit.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.guestlogix.travelercorekit.models.Pass;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.forms.Form;
import com.guestlogix.traveleruikit.forms.descriptors.InputDescriptor;
import com.guestlogix.traveleruikit.forms.descriptors.QuantityDescriptor;
import com.guestlogix.traveleruikit.forms.utilities.FormType;
import com.guestlogix.viewmodels.BookingViewModel;

import java.util.List;

/**
 * Fragment which lets the user to select any number of passes.
 */
public class PassSelectionFragment extends Fragment {

    private Form form;
    private TextView priceLbl;
    private Button bookNowBtn;
    private ProgressBar progressBar;

    private BookingViewModel viewModel;

    public PassSelectionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pass_selection, container, false);

        form = view.findViewById(R.id.passForm);
        priceLbl = view.findViewById(R.id.startingAtValueTextView);
        bookNowBtn = view.findViewById(R.id.bookNowBtn);
        progressBar = view.findViewById(R.id.bookNowPrgBar);

        bookNowBtn.setOnClickListener(this::onBookNowClick);

        // View Model
        viewModel = ViewModelProviders.of(getActivity()).get(BookingViewModel.class);
        viewModel.getPassesObservable().observe(this, this::onPasses);
        viewModel.getPriceChangeObservable().observe(this, this::onPriceChange);

        setFormObservers();

        return view;
    }

    private void onBookNowClick(View view) {
        // TODO
    }

    private void onPasses(List<Pass> passes) {
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
                return form.getType(FormType.QUANTITY); // Only have quantities for this fragment.
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
                // There is a bug with sending the value like this. Because it never gets updated.
                Pass p = passes.get(fieldId);
                QuantityDescriptor q = new QuantityDescriptor();

                q.maxQuantity = p.getMaxQuantity();
                q.minQuantity = 0;
                q.value = viewModel.getPassQuantity(p);
                q.title = p.getName();
                q.subtitle = p.getDescription();
                return q;
            }
        });
    }


    @SuppressLint("DefaultLocale")
    private void onPriceChange(Double price) {
        priceLbl.setText(String.format("$%.2f", price));
    }

    private void setFormObservers() {
        form.setOnFormValueChangedListener((sectionId, fieldId, value) -> {
            Integer i = (Integer) value; // Expecting integer for quantity pickers.
            if (i != null) {
                viewModel.updateValueForPass(fieldId, i);
            }
        });
    }

}
