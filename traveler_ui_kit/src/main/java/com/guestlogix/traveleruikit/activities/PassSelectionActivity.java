package com.guestlogix.traveleruikit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import com.guestlogix.travelercorekit.TravelerLog;
import com.guestlogix.travelercorekit.models.Pass;
import com.guestlogix.travelercorekit.models.Product;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.forms.Form;
import com.guestlogix.traveleruikit.forms.descriptors.InputDescriptor;
import com.guestlogix.traveleruikit.forms.descriptors.QuantityDescriptor;
import com.guestlogix.traveleruikit.viewmodels.PassSelectionViewModel;
import com.guestlogix.traveleruikit.widgets.ActionStrip;

import java.util.List;

/**
 * Activity which lets the user select an arbitrary amount of Passes for a particular product.
 * <p>
 * Requires a <code>List<Pass></code> object passes as "EXTRA_PASS_ACTIVITY_PASSES" extra and a <code>Product</code> object
 * passed as "EXTRA_PASS_ACTIVITY_PRODUCT". If either of those is null or missing the activity will finish with no result!
 * </p>
 */
public class PassSelectionActivity extends AppCompatActivity {

    /**
     * Expects a list of Pass objects.
     */
    public static final String EXTRA_PASSES = "EXTRA_PASS_ACTIVITY_PASSES";

    /**
     * Expects a Product object.
     */
    public static final String EXTRA_PRODUCT = "EXTRA_PASS_ACTIVITY_PRODUCT";

    private ActionStrip actionStrip;
    private Form form;

    private PassSelectionViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_selection);

        Bundle extras = getIntent().getExtras();
        form = findViewById(R.id.form_passSelectionActivity);
        actionStrip = findViewById(R.id.actionStrip_passSelectionActivity);

        if (null != extras && extras.containsKey(EXTRA_PASSES) && extras.containsKey(EXTRA_PRODUCT)) {
            List<Pass> passes = (List<Pass>) extras.getSerializable(EXTRA_PASSES);
            Product product = (Product) extras.getSerializable(EXTRA_PRODUCT);

            if (passes != null && product != null) {
                viewModel = ViewModelProviders.of(this).get(PassSelectionViewModel.class);
                viewModel.setup(product, passes);

                viewModel.getObservablePrice().observe(this, price -> actionStrip.setValue(price.getFormattedValue()));
                viewModel.getObservableBookingForm().observe(this, bookingForm -> {
                    Intent intent = new Intent(this, QuestionsActivity.class);
                    intent.putExtra(QuestionsActivity.EXTRA_BOOKING_FORM, bookingForm);
                    startActivity(intent);
                });

                viewModel.getObservablePasses().observe(this, this::onPasses);

                viewModel.getObservableState().observe(this, state -> {
                    switch (state) {
                        case DEFAULT:
                            actionStrip.changeState(ActionStrip.ActionStripState.ENABLED);
                            break;
                        case LOADING:
                            actionStrip.changeState(ActionStrip.ActionStripState.LOADING);
                            break;
                        case ERROR:
                            actionStrip.changeState(ActionStrip.ActionStripState.ENABLED);
                            AlertDialog.Builder b = new AlertDialog.Builder(PassSelectionActivity.this)
                                    .setMessage(R.string.unknown_error_message);

                            b.show();
                    }
                });

                actionStrip.setButtonText(getString(R.string.book_now));
                actionStrip.setLabel(getString(R.string.label_starting_at));
                actionStrip.setActionOnClickListener((v) -> viewModel.fetchBookingForm());

                return;
            }
        }

        TravelerLog.e("PassSelectionActivity requires a List<Pass> and a Product to operate.");
        finish();
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
                return Form.FormType.QUANTITY.getValue();
            }

            @NonNull
            @Override
            public InputDescriptor getInputDescriptor(int sectionId, int fieldId, int type) {
                QuantityDescriptor descriptor = new QuantityDescriptor();
                descriptor.minQuantity = 0;
                descriptor.title = passes.get(fieldId).getName();
                descriptor.subtitle = passes.get(fieldId).getDescription();

                return descriptor;
            }

            @Nullable
            @Override
            public String getTitle(int sectionId) {
                return null;
            }

            @Nullable
            @Override
            public String getDisclaimer(int sectionId) {
                return null;
            }

            @Nullable
            @Override
            public Pair<String, Form.FormMessage> getMessage(int sectionId, int fieldId) {
                return null;
            }

            @Override
            public Object getValue(int sectionId, int fieldId) {
                return viewModel.getValue(passes.get(fieldId));
            }

        });
        form.setOnFormValueChangedListener(((sectionId, fieldId, value) ->
                viewModel.updatePassQuantity(passes.get(fieldId), (Integer) value)));
    }
}
