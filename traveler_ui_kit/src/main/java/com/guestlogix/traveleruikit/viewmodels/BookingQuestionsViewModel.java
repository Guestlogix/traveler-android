package com.guestlogix.traveleruikit.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.guestlogix.travelercorekit.callbacks.OrderCreateCallback;
import com.guestlogix.travelercorekit.models.BookingForm;
import com.guestlogix.travelercorekit.models.Order;
import com.guestlogix.travelercorekit.models.Traveler;

import java.util.List;

public class BookingQuestionsViewModel extends StatefulViewModel {
    private MutableLiveData<BookingForm> bookingForm;
    private MutableLiveData<BookingForm.BookingFormError> error;
    private MutableLiveData<Order> order;

    private BookingForm.BookingFormError currentError;

    public BookingQuestionsViewModel() {
        bookingForm = new MutableLiveData<>();
        error = new MutableLiveData<>();
        order = new MutableLiveData<>();
    }

    public void setup(BookingForm bookingForm) {
        this.bookingForm.setValue(bookingForm);
    }

    public LiveData<BookingForm> getBookingForm() {
        return bookingForm;
    }

    public LiveData<BookingForm.BookingFormError> getError() {
        return error;
    }

    public LiveData<Order> getOrder() {
        return order;
    }

    // Add a better way to send messages. Maybe a specific model.
    public BookingForm.BookingFormError getMessage(int questionGroupId, int questionId) {
        if (currentError != null && questionGroupId == currentError.getGroupId() && questionId == currentError.getQuestionId()) {
            return currentError;
        }

        return null;
    }

    public void submit() {
        if (bookingForm.getValue() != null) {
            List<BookingForm.BookingFormError> errors = bookingForm.getValue().validate();

            if (!errors.isEmpty()) {
                // Notify the Activity with only the first error.
                currentError = errors.get(0);
                error.setValue(currentError);
            } else {
                status.setValue(State.LOADING);
                Traveler.createOrder(bookingForm.getValue(), new OrderCreateCallback() {
                    @Override
                    public void onOrderCreateSuccess(Order order) {
                        status.postValue(State.SUCCESS);
                        BookingQuestionsViewModel.this.order.postValue(order);
                    }

                    @Override
                    public void onOrderCreateFailure(Error error) {
                        status.postValue(State.ERROR);
                    }
                });
            }
        }
    }
}