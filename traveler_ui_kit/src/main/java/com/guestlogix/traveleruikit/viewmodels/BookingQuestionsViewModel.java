package com.guestlogix.traveleruikit.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.guestlogix.travelercorekit.models.BookingForm;

import java.util.List;

public class BookingQuestionsViewModel extends ViewModel {
    private MutableLiveData<BookingForm> bookingForm;
    private MutableLiveData<BookingForm.BookingFormError> error;

    private BookingForm.BookingFormError currentError;

    public BookingQuestionsViewModel() {
        bookingForm = new MutableLiveData<>();
        error = new MutableLiveData<>();
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

    // Add a better way to send messages. Maybe a specific model.
    public BookingForm.BookingFormError getMessage(int questionGroupId, int questionId) {
        if (questionGroupId == currentError.getGroupId() && questionId == currentError.getQuestionId()) {
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
                // Create Order
                //TODO
            }
        }
    }
}