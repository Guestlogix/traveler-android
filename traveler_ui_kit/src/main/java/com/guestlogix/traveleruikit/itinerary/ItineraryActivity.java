package com.guestlogix.traveleruikit.itinerary;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.guestlogix.travelercorekit.callbacks.ItineraryFetchCallback;
import com.guestlogix.travelercorekit.models.ItineraryItem;
import com.guestlogix.travelercorekit.models.ItineraryItemType;
import com.guestlogix.travelercorekit.models.ItineraryQuery;
import com.guestlogix.travelercorekit.models.ItineraryResult;
import com.guestlogix.travelercorekit.models.Range;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.fragments.LoadingFragment;
import com.guestlogix.traveleruikit.fragments.RetryFragment;
import com.guestlogix.traveleruikit.utils.FragmentTransactionQueue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ItineraryActivity extends AppCompatActivity implements ItineraryFetchCallback, RetryFragment.InteractionListener {
    public static String ARG_ITINERARY_QUERY = "ARG_ITINERARY_QUERY";

    private ItineraryQuery query;
    private FragmentTransactionQueue transactionQueue;
    ArrayList<ItineraryItemType> selectedItineraryItemTypes = new ArrayList<>();

    ArrayList<ItineraryItem> lastRetrievedItineraryItems = null;
    Date currentMinimumDate = null;
    Date currentMaximumDate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_itinerary);

        transactionQueue = new FragmentTransactionQueue(getSupportFragmentManager());

        query = (ItineraryQuery) getIntent().getSerializableExtra(ARG_ITINERARY_QUERY);

        if (query == null) {
            Log.e(this.getLocalClassName(), "a ItineraryQuery is required to run this activity");
            finish();
            return;
        }

        selectedItineraryItemTypes.add(ItineraryItemType.BOOKING);
        selectedItineraryItemTypes.add(ItineraryItemType.PARKING);

        findViewById(R.id.btnFilters).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastRetrievedItineraryItems == null)
                    return;

                //set start and end date
                List<ItineraryItem> sortedItineraryItems = getSortedItineraryItems(lastRetrievedItineraryItems);
                query.setStartDate(sortedItineraryItems.get(0).getStartDate());
                query.setEndDate(sortedItineraryItems.get(sortedItineraryItems.size() - 1).getEndDate());


                ItineraryFilters currentItineraryFilters = new ItineraryFilters(new Range<>(query.getStartDate(), query.getEndDate()), selectedItineraryItemTypes);


                new ItineraryFilterDialog(ItineraryActivity.this, currentItineraryFilters, new ItineraryFilterDialog.ItineraryFiltersCallback() {
                    @Override
                    public void onFiltersChanged(ItineraryFilters itineraryFilters) {
                        if (itineraryFilters.getDateRange().getLower().equals(currentItineraryFilters.getDateRange().getLower()) &&
                                itineraryFilters.getDateRange().getUpper().equals(currentItineraryFilters.getDateRange().getUpper())) {

                            if (!itineraryFilters.getSelectedItineraryItemTypes().equals(selectedItineraryItemTypes)) {
                                filterTheResultCategoriesAndShow(itineraryFilters.getSelectedItineraryItemTypes());
                                selectedItineraryItemTypes = itineraryFilters.getSelectedItineraryItemTypes();
                            }

                        } else {
                            query = new ItineraryQuery(query.getFlights(), itineraryFilters.getDateRange().getLower(), itineraryFilters.getDateRange().getUpper());
                            fetchItinerary();
                        }
                    }
                }).show();
            }
        });

        fetchItinerary();
    }

    private void fetchItinerary() {
        Fragment loadingFragment = new LoadingFragment();
        FragmentTransaction transaction = transactionQueue.newTransaction();
        transaction.replace(R.id.itineraryContainer, loadingFragment);
        transactionQueue.addTransaction(transaction);

        Traveler.fetchItinerary(query, this);
    }

    @Override
    public void onRetry() {
        fetchItinerary();
    }

    @Override
    public void onSuccess(ItineraryResult itineraryResult) {
        if (itineraryResult.getItems().size() == 0) {
            Toast.makeText(this, "Itinerary List is Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        lastRetrievedItineraryItems = itineraryResult.getItems();
        filterTheResultCategoriesAndShow(selectedItineraryItemTypes);
    }

    @NonNull
    private List<ItineraryItem> getSortedItineraryItems(List<ItineraryItem> itineraryItems) {
        List<ItineraryItem> clonedItineraryList = new ArrayList<>(itineraryItems);
        Collections.sort(clonedItineraryList, new Comparator<ItineraryItem>() {
            @Override
            public int compare(ItineraryItem o1, ItineraryItem o2) {
                Calendar c1 = Calendar.getInstance();
                c1.setTime(o1.getStartDate());
                Calendar c2 = Calendar.getInstance();
                c1.setTime(o2.getStartDate());
                if (c1.get(Calendar.YEAR) < c2.get(Calendar.YEAR)) {
                    return -1;
                } else if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) {
                    if (c1.get(Calendar.DAY_OF_YEAR) < c2.get(Calendar.DAY_OF_YEAR)) {
                        return -1;
                    } else if (c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)) {
                        return 0;
                    } else {
                        return 1;
                    }
                } else {
                    return 1;
                }
            }
        });
        return clonedItineraryList;
    }

    @Override
    public void onError(Error error) {
        RetryFragment errorFragment = new RetryFragment();
        FragmentTransaction transaction = transactionQueue.newTransaction();
        transaction.replace(R.id.itineraryContainer, errorFragment);
        transactionQueue.addTransaction(transaction);
    }

    private void filterTheResultCategoriesAndShow(ArrayList<ItineraryItemType> lstCurrentlySelectedItineraryItemTypes) {
        ArrayList<ItineraryItem> filteredItineraryItems = new ArrayList<>();
        for (ItineraryItem itineraryItem : lastRetrievedItineraryItems) {
            if (lstCurrentlySelectedItineraryItemTypes.contains(itineraryItem.getItineraryItemType()))
                filteredItineraryItems.add(itineraryItem);
        }
        Fragment ordersFragment = ItineraryFragment.newInstance(filteredItineraryItems);
        FragmentTransaction transaction = transactionQueue.newTransaction();
        transaction.replace(R.id.itineraryContainer, ordersFragment);
        transactionQueue.addTransaction(transaction);
    }
}
