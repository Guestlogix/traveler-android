package com.guestlogix.traveleruikit.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.travelercorekit.TravelerLog;
import com.guestlogix.travelercorekit.callbacks.FetchBookingFormCallback;
import com.guestlogix.travelercorekit.models.*;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.forms.FormHeader;
import com.guestlogix.traveleruikit.forms.FormMessage;
import com.guestlogix.traveleruikit.forms.Form;
import com.guestlogix.traveleruikit.forms.models.FormModel;
import com.guestlogix.traveleruikit.forms.models.QuantityFormModel;
import com.guestlogix.traveleruikit.widgets.ActionStrip;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Activity which lets the user select an arbitrary amount of Passes for a particular product.
 * <p>
 * Requires a <code>List&lt;Pass&gt;</code> object passes as "EXTRA_PASS_ACTIVITY_PASSES" extra and a <code>Product</code> object
 * passed as "EXTRA_PASS_ACTIVITY_PRODUCT". If either of those is null or missing the activity will finish with no result!
 * </p>
 */
public class PassSelectionActivity extends AppCompatActivity implements
        Form.DataSource,
        Form.FormValueChangedListener,
        FetchBookingFormCallback {

    /**
     * Expects a list of Pass objects.
     */
    public static final String EXTRA_PASSES = "EXTRA_PASS_ACTIVITY_PASSES";

    /**
     * Expects a Product object.
     */
    public static final String EXTRA_PRODUCT = "EXTRA_PASS_ACTIVITY_PRODUCT";

    /**
     * Optional String representing the selected flavour.
     */
    public static final String EXTRA_FLAVOUR = "EXTRA_FLAVOUR";

    // Data
    private List<Pass> passes;
    private Product product;
    private Map<Pass, Integer> passQuantities;
    private String flavourTitle;

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
            flavourTitle = (String) savedInstanceState.getSerializable(EXTRA_FLAVOUR);
        }

        if (passes == null || product == null) {
            // Attempt to read them from intent.
            Bundle extras = getIntent().getExtras();

            if (null != extras && extras.containsKey(EXTRA_PASSES) && extras.containsKey(EXTRA_PRODUCT)) {
                passes = (List<Pass>) extras.getSerializable(EXTRA_PASSES);
                product = (Product) extras.getSerializable(EXTRA_PRODUCT);
                flavourTitle = (String) extras.getSerializable(EXTRA_FLAVOUR);
            }
        }

        if (passes == null || product == null) {
            TravelerLog.e("PassSelectionActivity requires a List<Pass> and a Product to operate.");
            finish();
            return;
        }

        passQuantities = new HashMap<>();

        LinearLayoutManager lm = new LinearLayoutManager(this);

        form = findViewById(R.id.form_passSelectionActivity);
        form.setDataSource(this);
        form.setLayoutManager(lm);
        form.setFormValueChangedListener(this);
        form.addItemDecoration(new PassItemDecoration(this));

        if (flavourTitle != null) {
            TextView tv = findViewById(R.id.textView_passSelectionActivity_flavourTitle);
            tv.setText(flavourTitle);
        }

        actionStrip = findViewById(R.id.actionStrip_passSelectionActivity);
        actionStrip.setButtonText(getString(R.string.book_now));
        actionStrip.setLabel(getString(R.string.label_starting_at));
        actionStrip.setActionOnClickListener(this::onActionStripClick);
        actionStrip.changeState(ActionStrip.ActionStripState.DISABLED);

        calculatePrice();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public int getSectionCount() {
        return 1;
    }

    @Override
    public int getFieldCount(int sectionId) {
        return passes == null ? 0 : passes.size();
    }

    @NonNull
    @Override
    public FormModel getModel(int sectionId, int fieldId) {
        Pass pass = passes.get(fieldId);
        return new QuantityFormModel(pass.getName(), pass.getDescription(), 10, 0);
    }

    @Nullable
    @Override
    public FormHeader getSectionHeader(int sectionId) {
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
        Pass pass = passes.get(fieldId);

        Integer value = passQuantities.get(pass);

        if (value == null) {
            value = 0;
        }

        return value;
    }

    @Override
    public void onFormValueChanged(int _sectionId, int fieldId, Object value) {
        Pass pass = passes.get(fieldId);
        Integer val = (Integer) value;

        if (val == null) {
            val = 0;
        }

        passQuantities.put(pass, val);

        enableActionStrip();
        calculatePrice();
    }

    @Override
    public void onBookingFormFetchSuccess(BookingForm bookingForm) {
        Intent intent = new Intent(this, QuestionsActivity.class);
        intent.putExtra(QuestionsActivity.EXTRA_BOOKING_FORM, bookingForm);
        startActivity(intent);
    }

    @Override
    public void onBookingFormFetchError(Error error) {
        actionStrip.changeState(ActionStrip.ActionStripState.ENABLED);
        new AlertDialog.Builder(this)
                .setMessage(error.getMessage())
                .show();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable(EXTRA_PRODUCT, product);
        outState.putSerializable(EXTRA_PASSES, (Serializable) passes);

        if (flavourTitle != null) {
            outState.putSerializable(EXTRA_FLAVOUR, flavourTitle);
        }

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableActionStrip();
    }

    private void enableActionStrip() {
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
    }

    private class PassItemDecoration extends RecyclerView.ItemDecoration {
        private final int MARGIN_IN_DIPS = (int) PassSelectionActivity.this.getResources().getDimension(R.dimen.margin_default);
        private Drawable divider;
        private Rect bounds = new Rect();

        PassItemDecoration(Context context) {
            final TypedArray a = context.obtainStyledAttributes(new int[]{android.R.attr.listDivider});
            divider = a.getDrawable(0);

            a.recycle();
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);

            outRect.set(MARGIN_IN_DIPS, outRect.top, MARGIN_IN_DIPS, outRect.bottom);
        }

        @Override
        public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            c.save();
            int l, r;

            if (parent.getClipToPadding()) {
                l = parent.getPaddingLeft();
                r = parent.getWidth() - parent.getPaddingRight();

                c.clipRect(l, parent.getPaddingTop(), r, parent.getHeight() - parent.getPaddingBottom());
            } else {
                l = 0;
                r = parent.getWidth();
            }

            for (int i = 0; i < parent.getChildCount(); i++) {
                View v = parent.getChildAt(i);
                parent.getDecoratedBoundsWithMargins(v, bounds);

                int translationY = Math.round(v.getTranslationY());
                int t = bounds.top + translationY;
                int b = t + divider.getIntrinsicHeight();

                divider.setBounds(l, t, r, b);
                divider.draw(c);

                if (i == parent.getChildCount() - 1) {
                    // Last item, draw a line under.

                    int b2 = bounds.bottom + translationY;
                    int t2 = b2 - divider.getIntrinsicHeight();

                    divider.setBounds(l, t2, r, b2);
                    divider.draw(c);
                }
            }

            c.restore();
        }
    }
}
