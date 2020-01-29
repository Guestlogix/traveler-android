package com.guestlogix.traveleruikit.itinerary;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.guestlogix.travelercorekit.models.ItineraryItem;
import com.guestlogix.travelercorekit.models.ItineraryItemType;
import com.guestlogix.travelercorekit.models.ProductType;
import com.guestlogix.travelercorekit.models.PurchasedProductQuery;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.purchasedproduct.PurchasedBookingProductActivity;
import com.guestlogix.traveleruikit.purchasedproduct.PurchasedParkingProductActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class ItineraryFragment extends Fragment implements ItineraryAdapter.OnItemClickListener {
    public static final String ARG_ITINERARY_ITEM_LIST = "ARG_ITINERARY_ITEM_LIST";
    private static final int REQUEST_CODE_DETAILS = 1;

    private ArrayList<ItineraryItem> itineraryItems;
    private ItineraryGroupAdapter adapter;

    public static ItineraryFragment newInstance(ArrayList<ItineraryItem> itineraryItems) {
        ItineraryFragment fragment = new ItineraryFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ITINERARY_ITEM_LIST, itineraryItems);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itineraryItems = (ArrayList<ItineraryItem>) getArguments().getSerializable(ARG_ITINERARY_ITEM_LIST);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_itinerary, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.rvItineraries);

        List<ItineraryGroup> itineraryGroups = groupAndSortItineraries();

        adapter = new ItineraryGroupAdapter(getContext(), itineraryGroups, this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private List<ItineraryGroup> groupAndSortItineraries() {
        List<ItineraryGroup> itineraryGroups = new ArrayList<>();

        //group the itineraries
        HashMap<String, List<ItineraryItem>> dateGroupedItineraries = new HashMap<>();
        for (ItineraryItem itineraryItem : itineraryItems) {
            Calendar itineraryDate = Calendar.getInstance();
            itineraryDate.setTime(itineraryItem.getStartDate());


            String key = itineraryDate.get(Calendar.YEAR) + "#" + itineraryDate.get(Calendar.DAY_OF_YEAR);
            if (dateGroupedItineraries.containsKey(key)) {
                dateGroupedItineraries.get(key).add(itineraryItem);
            } else {
                ArrayList<ItineraryItem> itineraries = new ArrayList<>();
                itineraries.add(itineraryItem);
                dateGroupedItineraries.put(key, itineraries);
            }
        }


        for (String stringDate : dateGroupedItineraries.keySet()) {
            itineraryGroups.add(new ItineraryGroup(dateGroupedItineraries.get(stringDate).get(0).getStartDate(), dateGroupedItineraries.get(stringDate)));
        }

        //sort the itineraries list
        Collections.sort(itineraryGroups, new Comparator<ItineraryGroup>() {
            @Override
            public int compare(ItineraryGroup o1, ItineraryGroup o2) {
                Calendar c1 = Calendar.getInstance();
                c1.setTime(o1.getGroupDate());
                Calendar c2 = Calendar.getInstance();
                c2.setTime(o2.getGroupDate());
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

        return itineraryGroups;
    }

    @Override
    public void onItemClick(ItineraryItem itineraryItem) {

        if (itineraryItem.getItineraryItemType() == ItineraryItemType.BOOKING) {
            Intent intent = new Intent(getContext(), PurchasedBookingProductActivity.class);
            intent.putExtra(PurchasedBookingProductActivity.ARG_PURCHASED_PRODUCT_QUERY, new PurchasedProductQuery(itineraryItem.getOrderId(), itineraryItem.getId(), ProductType.BOOKABLE));
            startActivity(intent);
        } else if (itineraryItem.getItineraryItemType() == ItineraryItemType.PARKING) {
            Intent intent = new Intent(getContext(), PurchasedParkingProductActivity.class);
            intent.putExtra(PurchasedParkingProductActivity.ARG_PURCHASED_PRODUCT_QUERY, new PurchasedProductQuery(itineraryItem.getOrderId(), itineraryItem.getId(), ProductType.BOOKABLE));
            startActivity(intent);
        }


    }
}
