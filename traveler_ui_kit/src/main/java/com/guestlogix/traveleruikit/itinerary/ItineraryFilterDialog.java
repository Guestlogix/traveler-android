package com.guestlogix.traveleruikit.itinerary;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.guestlogix.travelercorekit.models.ItineraryItemType;
import com.guestlogix.travelercorekit.models.Range;
import com.guestlogix.travelercorekit.utilities.DateHelper;
import com.guestlogix.traveleruikit.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class ItineraryFilterDialog extends Dialog {

    private ItineraryFiltersCallback itineraryFiltersCallback;
    private ItineraryFilters currentFilter;

    public ItineraryFilterDialog(@NonNull Context context, ItineraryFilters currentFilter, ItineraryFiltersCallback itineraryFiltersCallback) {
        super(context);
        this.itineraryFiltersCallback = itineraryFiltersCallback;
        this.currentFilter = new ItineraryFilters(currentFilter.getDateRange(), currentFilter.getSelectedItineraryItemTypes());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_itinerary_filters);
        TextView etStartDate = findViewById(R.id.etStartDate);
        TextView etEndDate = findViewById(R.id.etEndDate);

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(currentFilter.getDateRange().getLower());

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(currentFilter.getDateRange().getUpper());

        etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        currentFilter.setDateRange(new Range<Date>(new GregorianCalendar(year, month, dayOfMonth).getTime(), currentFilter.getDateRange().getUpper()));
                        etStartDate.setText(DateHelper.formatDate(currentFilter.getDateRange().getLower()));
                    }
                }, startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        currentFilter.setDateRange(new Range<Date>(currentFilter.getDateRange().getLower(), new GregorianCalendar(year, month, dayOfMonth).getTime()));
                        etEndDate.setText(DateHelper.formatDate(currentFilter.getDateRange().getUpper()));
                    }
                }, endCalendar.get(Calendar.YEAR), endCalendar.get(Calendar.MONTH), endCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        RecyclerView rvCategories = findViewById(R.id.rvCategories);


        etStartDate.setText(DateHelper.formatDate(currentFilter.getDateRange().getLower()));
        etEndDate.setText(DateHelper.formatDate(currentFilter.getDateRange().getUpper()));

        List<ItineraryItemType> lstAvailableCategories = new ArrayList<>();
        lstAvailableCategories.add(ItineraryItemType.BOOKING);
        lstAvailableCategories.add(ItineraryItemType.PARKING);
        lstAvailableCategories.add(ItineraryItemType.FLIGHT);

        ItineraryItemTypesAdapter itineraryItemTypesAdapter = new ItineraryItemTypesAdapter(lstAvailableCategories, currentFilter.getSelectedItineraryItemTypes());
        rvCategories.setAdapter(itineraryItemTypesAdapter);

        rvCategories.setLayoutManager(new LinearLayoutManager(getContext()));

        findViewById(R.id.btnApply).setOnClickListener(v -> {
            currentFilter.setSelectedProductTypes(itineraryItemTypesAdapter.getSelectedCategories());
            itineraryFiltersCallback.onFiltersChanged(currentFilter);
            dismiss();
        });
    }

    public interface ItineraryFiltersCallback {
        void onFiltersChanged(ItineraryFilters itineraryFilters);
    }
}
