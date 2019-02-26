package com.guestlogix.traveleruikit.viewmodels;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.guestlogix.travelercorekit.callbacks.FetchPassesCallback;
import com.guestlogix.travelercorekit.error.TravelerError;
import com.guestlogix.travelercorekit.models.*;
import com.guestlogix.travelercorekit.utilities.TravelerLog;
import com.guestlogix.traveleruikit.repositories.BookingRepository;
import com.guestlogix.traveleruikit.utils.SingleLiveEvent;

import java.util.*;

public class BookingViewModel extends ViewModel {

    private BookingContext bookingContext;
    private BookingForm bookingForm;
    private BookingRepository bookingRepository;
    private Map<Pass, Integer> passQuantityMap;

    private MutableLiveData<List<Pass>> passesData;
    private MutableLiveData<Double> priceChange;
    private MutableLiveData<State> state;
    private SingleLiveEvent<Event<Pair<Integer, Integer>>> bookingFormError;
    private MutableLiveData<BookingForm> bookingFormData;


    public BookingViewModel() {
        bookingRepository = new BookingRepository();
        passesData = new MutableLiveData<>();
        state = new MutableLiveData<>();
        priceChange = new MutableLiveData<>();
        bookingFormError = new SingleLiveEvent<>();
        bookingFormData = new MutableLiveData<>();
    }

    public void setBookingContext(BookingContext bookingContext) {
        this.bookingContext = bookingContext;
        updatePass(this.bookingContext);
    }

    public LiveData<List<Pass>> getObservablePasses() {
        return passesData;
    }

    public LiveData<Double> getObservablePrice() {
        return priceChange;
    }

    public LiveData<State> getObservableStatus() {
        return state;
    }

    public LiveData<Event<Pair<Integer, Integer>>> getObservableBookingFormErrorPosition() {
        return bookingFormError;
    }

    public LiveData<BookingForm> getObservableBookingForm() {
        return bookingFormData;
    }

    public BookingForm getBookingFormObject() {
        return bookingForm;
    }


    public int getPassQuantity(Pass pass) {
        Integer quantity = passQuantityMap.get(pass);

        if (quantity == null) {
            quantity = 0;
        }

        return quantity;
    }

    public void submitPasses() {
        bookingForm = new BookingForm(flattenMap());

        state.setValue(State.QUESTIONS);
        bookingFormData.setValue(bookingForm);
    }

    public void submitQuestions() {
        state.setValue(State.LOADING);
        Pair<Integer, Integer> firstError = bookingForm.validate();

        if (firstError != null) {
            state.setValue(State.QUESTIONS);
            bookingFormError.setValue(new Event<>(firstError));
        } else {
            state.setValue(State.SUCCESS);
        }

    }

    public void updateValueForPass(Pass pass, Integer newQuantity) {
        if (passQuantityMap == null) {
            return;
        }

        int quantity = 0;

        // Null check.
        if (newQuantity != null) {
            quantity = newQuantity;
        }

        passQuantityMap.put(pass, quantity);
        calculateTotalPrice();
    }

    public void updateValueForQuestion(@NonNull Question question, Object value) {
        QuestionType type = question.getType();
        Answer answer;
        switch (type) {
            case STRING:
                answer = new TextualAnswer(value.toString(), question);
                bookingForm.addAnswer(answer, question);
                break;
            case MULTIPLE_CHOICE:
                answer = new MultipleChoiceSelection((Integer) value, question);
                bookingForm.addAnswer(answer, question);
                break;
        }
    }

    private List<Pass> flattenMap() {
        List<Pass> passes = new ArrayList<>();
        for (Map.Entry<Pass, Integer> entry : passQuantityMap.entrySet()) {
            Integer value = entry.getValue();

            for (int i = 0; i < value; i++) {
                passes.add(entry.getKey());
            }
        }
        return passes;
    }

    private void updatePass(BookingContext bookingContext) {
        state.setValue(State.LOADING);
        bookingRepository.fetchPasses(bookingContext, fetchPassesCallback);
    }

    private void calculateTotalPrice() {
        double price = 0.0;

        for (Map.Entry<Pass, Integer> e : passQuantityMap.entrySet()) {
            Integer quantity = e.getValue();
            Pass pass = e.getKey();

            // Prevents NPE
            if (quantity == null || quantity < 0) {
                quantity = 0;
            }

            price += (quantity * pass.getPrice().getValue());
        }

        priceChange.postValue(price);
    }

    private FetchPassesCallback fetchPassesCallback = new FetchPassesCallback() {
        @Override
        public void onSuccess(List<Pass> passes) {
            passQuantityMap = new HashMap<>();

            // Initialize all quantities to 0.
            for (Pass pass : passes) {
                passQuantityMap.put(pass, 0);
            }

            calculateTotalPrice();
            passesData.postValue(passes);
            state.postValue(State.PASS_SELECTION);
        }

        @Override
        public void onError(TravelerError error) {
            state.setValue(State.ERROR);
        }
    };

    public enum State {
        ERROR, LOADING, PASS_SELECTION, QUESTIONS, SUCCESS
    }

    // Limits the event to one handler.
    // Can be replaced with live data for this case.
    public class Event<T> {
        private boolean hasBeenHandled = false;
        private final T data;

        public Event(T data) {
            this.data = data;
        }

        @Nullable
        public T getData() {
            if (hasBeenHandled) {
                return null;
            } else {
                hasBeenHandled = true;
                return data;
            }
        }

        public T peekData() {
            return data;
        }
    }
}
