package com.guestlogix.traveleruikit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.guestlogix.travelercorekit.TravelerLog;
import com.guestlogix.travelercorekit.callbacks.FetchBookingFormCallback;
import com.guestlogix.travelercorekit.models.*;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.forms.Form;
import com.guestlogix.traveleruikit.forms.descriptors.InputDescriptor;
import com.guestlogix.traveleruikit.widgets.ActionStrip;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Activity which lets the user select an arbitrary amount of Passes for a particular product.
 * <p>
 * Requires a <code>List<Pass></code> object passes as "EXTRA_PASS_ACTIVITY_PASSES" extra and a <code>Product</code> object
 * passed as "EXTRA_PASS_ACTIVITY_PRODUCT". If either of those is null or missing the activity will finish with no result!
 * </p>
 */
public class PassSelectionActivity extends AppCompatActivity implements Form.DataSource, Form.OnFormValueChangedListener, FetchBookingFormCallback {

    /**
     * Expects a list of Pass objects.
     */
    public static final String EXTRA_PASSES = "EXTRA_PASS_ACTIVITY_PASSES";

    /**
     * Expects a Product object.
     */
    public static final String EXTRA_PRODUCT = "EXTRA_PASS_ACTIVITY_PRODUCT";

    // Data
    private List<Pass> passes;
    private Product product;
    private Map<Pass, Integer> passQuantities;

    // Views
    private ActionStrip actionStrip;
    private Form form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_selection);

        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_PASSES) && savedInstanceState.containsKey(EXTRA_PRODUCT)) {
            passes = (List<Pass>) savedInstanceState.getSerializable(EXTRA_PASSES);
            product = (Product) savedInstanceState.getSerializable(EXTRA_PRODUCT);
        }

        if (passes == null || product == null) {
            // Attempt to read them from intent.
            Bundle extras = getIntent().getExtras();

            if (null != extras && extras.containsKey(EXTRA_PASSES) && extras.containsKey(EXTRA_PRODUCT)) {
                passes = (List<Pass>) extras.getSerializable(EXTRA_PASSES);
                product = (Product) extras.getSerializable(EXTRA_PRODUCT);
            }
        }

        if (passes == null || product == null) {
            TravelerLog.e("PassSelectionActivity requires a List<Pass> and a Product to operate.");
            finish();
            return;
        }

        passQuantities = new HashMap<>();

        form = findViewById(R.id.form_passSelectionActivity);
        form.setDataSource(this);
        form.setOnFormValueChangedListener(this);
        form.reload();

        actionStrip = findViewById(R.id.actionStrip_passSelectionActivity);
        actionStrip.setButtonText(getString(R.string.book_now));
        actionStrip.setLabel(getString(R.string.label_starting_at));
        actionStrip.setActionOnClickListener(this::onActionStripClick);
        actionStrip.changeState(ActionStrip.ActionStripState.DISABLED);

        calculatePrice();
    }

    @Override
    public int getSectionCount() {
        return 1;
    }

    @Override
    public int getFieldCount(int sectionId) {
        return passes == null ? 0 : passes.size();
    }

    @Override
    public int getType(int sectionId, int fieldId) {
        return Form.FormType.QUANTITY.getValue();
    }

    @NonNull
    @Override
    public InputDescriptor getInputDescriptor(int _sectionId, int fieldId, int _type) {
        InputDescriptor descriptor = new InputDescriptor();
        Pass pass = passes.get(fieldId);

        descriptor.title = pass.getName();
        descriptor.subtitle = pass.getDescription();

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

    @Nullable
    @Override
    public Object getValue(int sectionId, int fieldId) {
        Pass pass = passes.get(sectionId);

        Integer value = passQuantities.get(pass);

        if (value == null) {
            value = 0;
        }

        return value;
    }

    @Override
    public void onFormValueChanged(int sectionId, int _fieldId, Object value) {
        Pass pass = passes.get(sectionId);
        Integer val = (Integer) value;

        if (val == null) {
            val = 0;
        }

        passQuantities.put(pass, val);

        boolean hasAtLeastOnePass = false;
        for (Integer i : passQuantities.values()) {
            if (i != null && i > 0) {
                hasAtLeastOnePass = true;
                break;
            }
        }

        if (hasAtLeastOnePass) {
            actionStrip.changeState(ActionStrip.ActionStripState.ENABLED);
        } else {
            actionStrip.changeState(ActionStrip.ActionStripState.DISABLED);
        }


        calculatePrice();
    }

    @Override
    public void onBookingFormFetchSuccess(BookingForm bookingForm) {
        Intent intent = new Intent(this, QuestionsActivity.class);
        intent.putExtra(QuestionsActivity.EXTRA_BOOKING_FORM, bookingForm);
        startActivity(intent);
        actionStrip.changeState(ActionStrip.ActionStripState.ENABLED);
    }

    @Override
    public void onBookingFormFetchError(Error error) {
        actionStrip.changeState(ActionStrip.ActionStripState.ENABLED);
        new AlertDialog.Builder(this)
                .setMessage(R.string.unexpected_error)
                .show();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable(EXTRA_PRODUCT, product);
        outState.putSerializable(EXTRA_PASSES, (Serializable) passes);
        super.onSaveInstanceState(outState);
    }

    private void onActionStripClick(View _view) {
        actionStrip.changeState(ActionStrip.ActionStripState.LOADING);
        List<Pass> flatPasses = new ArrayList<>();

        for (Map.Entry<Pass, Integer> entry : passQuantities.entrySet()) {
            if (entry.getValue() != null && entry.getValue() > 0) {
                for (int i = 0; i < entry.getValue(); i++) {
                    flatPasses.add(entry.getKey());
                }
            }
        }

        Traveler.fetchBookingForm(product, flatPasses, this);
    }

    // TODO: Extract this to a model and make back-end calculate the price
    private void calculatePrice() {
        double sum = 0.0;
        String currency = null;

        for (Map.Entry<Pass, Integer> entry : passQuantities.entrySet()) {
            if (entry.getValue() != null && entry.getValue() > 0) {
                sum += entry.getKey().getPrice().getValue() * entry.getValue();

                if (null == currency) {
                    currency = entry.getKey().getPrice().getCurrency();
                } else {
                    if (!currency.equalsIgnoreCase(entry.getKey().getPrice().getCurrency())) {
                        // TODO: handle adding different currencies.
                        return;
                    }
                }
            }
        }

        // If currency is still null use the currency of the product
        if (currency == null) {
            currency = product.getPrice().getCurrency();
        }

        Price price = new Price(sum, currency);
        actionStrip.setValue(price.getFormattedValue());
    }
}
