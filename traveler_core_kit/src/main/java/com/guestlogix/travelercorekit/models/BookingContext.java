package com.guestlogix.travelercorekit.models;

import java.util.Date;

public class BookingContext {

    Product product;
    Date selectedDate;
    Long selectedTime;
    Date endDateTime;//TODO
    Boolean isTimeRequired;

    public BookingContext(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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

    public void setTimeRequired(Boolean timeRequired) {
        isTimeRequired = timeRequired;
    }
}
