package com.guestlogix.traveler.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.traveler.adapters.FlightCardsRecyclerViewAdapter;
import com.guestlogix.travelercorekit.models.Flight;

import java.util.List;

public class FlightCardsRecyclerView extends RecyclerView implements View.OnClickListener {
    private FlightCardsRecyclerViewAdapter adapter;
    private LinearLayoutManager layoutManager;

    private OnAddFlightListener onAddFlightListener;

    public FlightCardsRecyclerView(@NonNull Context context) {
        super(context);
        init();
    }

    public FlightCardsRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FlightCardsRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setFlights(List<Flight> flights) {
        adapter.update(flights);
    }

    public void setOnAddFlightListener(OnAddFlightListener onAddFlightListener) {
        this.onAddFlightListener = onAddFlightListener;
        adapter.setFlightAddingEnabled(this.onAddFlightListener != null);
    }

    @Override
    public void onClick(View v) {
        int index = (Integer) v.getTag();

        if (onAddFlightListener != null) {
            onAddFlightListener.onFlightAdded(index);
        }
    }

    private void init() {
        this.adapter = new FlightCardsRecyclerViewAdapter();
        this.layoutManager = new LinearLayoutManager(getContext());

        setAdapter(adapter);
        setLayoutManager(layoutManager);

        adapter.setAddFlightOnClickListener(this);
    }

    /**
     * Callback interface used to dispatch add flight events.
     */
    public interface OnAddFlightListener {
        /**
         * Called when a flight needs to be added.
         *
         * @param index Position of the flight
         */
        void onFlightAdded(int index);
    }
}
