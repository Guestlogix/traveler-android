package com.guestlogix.travelercorekit.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class BookingContext implements Serializable {
    private Product product;
    private Date selectedDate;
    private Long selectedTime;
    private Availability availability;

    public BookingContext(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    public Date getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }

    public Long getSelectedTime() {
        return selectedTime;
    }

    public List<Long> getAvailableTimes() {
        return availability.getTimes();
    }

    public void setSelectedTime(Long selectedTime) {
        this.selectedTime = selectedTime;
    }

    public boolean requiresTime() {
        return availability != null && availability.getTimes() != null && !availability.getTimes().isEmpty();
    }

    public boolean hasAvailability() {
        return availability != null && availability.isAvailable();
    }

    public boolean isReady() {
        if (selectedDate == null) {
            return false;
        }

        if (hasAvailability() && !requiresTime()) {
            return true;
        }

        return !hasAvailability() || !requiresTime() || selectedTime != null;
    }

    void setAvailability(Availability availability) {
        this.availability = availability;
    }
}
