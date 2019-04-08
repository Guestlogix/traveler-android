package com.guestlogix.traveleruikit.models;

import androidx.annotation.Nullable;
import com.guestlogix.travelercorekit.models.Availability;
import com.guestlogix.travelercorekit.models.BookingOption;
import com.guestlogix.travelercorekit.models.Price;
import com.guestlogix.travelercorekit.models.Product;

import java.util.List;

public class BookingContext implements PurchaseContext {
    private Product product;

    private Availability availability;
    private BookingOption option;

    private State state;
    private List<BookingOption> options;
    private String optionsTitle;

    transient private BookingContextUpdateListener updateListener;

    public BookingContext(Product product) {
        this.product = product;
        state = State.DEFAULT;
    }

    public Availability getAvailability() {
        return availability;
    }

    public BookingOption getOption() {
        return option;
    }

    public void setOption(int pos) {
        if (state == State.OPTION_REQUIRED) {
            this.option = options.get(pos);
            state = State.AVAILABLE;
            update();
        }
    }

    public Product getProduct() {
        return product;
    }

    public List<BookingOption> getOptions() {
        return options;
    }

    public String getOptionsTitle() {
        return optionsTitle;
    }

    @Nullable
    @Override
    public Price getPrice() {
        return product != null ? product.getPrice() : null;
    }

    @Override
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
        update();
    }

    public void setAvailabilities(List<Availability> availabilities) {
        if (availabilities == null || availabilities.isEmpty()) {
            state = State.NOT_AVAILABLE;
            update();
            return;
        }

        availability = availabilities.get(0);
        option = null;

        if (availability.getBookingOptionSet() == null || availability.getBookingOptionSet().getOptions() == null || availability.getBookingOptionSet().getOptions().isEmpty()) {
            state = State.AVAILABLE;
            update();
            return;
        }

        options = availability.getBookingOptionSet().getOptions();
        optionsTitle = availability.getBookingOptionSet().getLabel();
        state = State.OPTION_REQUIRED;
        update();
    }

    public void setUpdateListener(BookingContextUpdateListener updateListener) {
        this.updateListener = updateListener;
    }

    private void update() {
        if (updateListener != null) {
            updateListener.onBookingContextUpdate(this);
        }
    }

    public interface BookingContextUpdateListener {
        void onBookingContextUpdate(BookingContext bookingContext);
    }
}
