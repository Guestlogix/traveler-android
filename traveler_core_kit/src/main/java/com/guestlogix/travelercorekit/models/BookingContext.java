package com.guestlogix.travelercorekit.models;

import java.io.Serializable;
import java.util.Date;

public class BookingContext implements Serializable {

    private Product product;
    private Date selectedDate;
    private Long selectedTime;
    private Date endDateTime;
    private Boolean isTimeRequired;
    private Boolean isReady;

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

    public Date getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }

    public Long getSelectedTime() {
        return selectedTime;
    }

    public void setSelectedTime(Long selectedTime) {
        this.selectedTime = selectedTime;
    }

    public Boolean getTimeRequired() {
        return isTimeRequired;
    }

    public Boolean isReady() {
        return isReady;
    }

    public void setReady(Boolean ready) {
        isReady = ready;
    }

    public void setTimeRequired(Boolean timeRequired) {
        isTimeRequired = timeRequired;
    }
}
