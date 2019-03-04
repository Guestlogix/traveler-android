package com.guestlogix.traveleruikit.viewmodels;

import android.util.Pair;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.guestlogix.travelercorekit.callbacks.FetchPassesCallback;
import com.guestlogix.travelercorekit.models.*;
import com.guestlogix.traveleruikit.repositories.BookingRepository;
import com.guestlogix.traveleruikit.utils.SingleLiveEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingViewModel extends ViewModel {

    private BookingContext bookingContext;
    private BookingForm bookingForm;
    private BookingRepository bookingRepository;
    private Map<Pass, Integer> passQuantityMap;
    private BookingForm.BookingFormError currentError;

    private MutableLiveData<List<Pass>> passesData;
    private MutableLiveData<Price> priceChange;
    private MutableLiveData<State> state;
    private SingleLiveEvent<Pair<Integer, Integer>> formFocus;
    private MutableLiveData<BookingForm> bookingFormData;


    public BookingViewModel() {
        bookingRepository = new BookingRepository();
        passesData = new MutableLiveData<>();
        state = new MutableLiveData<>();
        priceChange = new MutableLiveData<>();
        formFocus = new SingleLiveEvent<>();
        bookingFormData = new MutableLiveData<>();
    }

    public void setBookingContext(BookingContext bookingContext) {
        this.bookingContext = bookingContext;
        updatePass(this.bookingContext);
    }

    public LiveData<List<Pass>> getObservablePasses() {
        return passesData;
    }

    public LiveData<Price> getObservablePrice() {
        return priceChange;
    }

    public LiveData<State> getObservableStatus() {
        return state;
    }

    public LiveData<Pair<Integer, Integer>> getObservableBookingFormErrorPosition() {
        return formFocus;
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

        currentError = null;
        state.setValue(State.QUESTIONS);
        bookingFormData.setValue(bookingForm);
    }

    public void submitQuestions() {
        state.setValue(State.LOADING);
        List<BookingForm.BookingFormError> errors = bookingForm.validate();

        if (!errors.isEmpty()) {
            state.setValue(State.QUESTIONS);
            currentError = errors.get(0);
            formFocus.setValue(new Pair<>(currentError.groupId, currentError.questionId));
        } else {
            state.setValue(State.SUCCESS);
        }

    }

    @Nullable
    public BookingForm.BookingFormError getCurrentError(int sectionId, int questionId) {
        if (this.currentError != null && this.currentError.groupId == sectionId && this.currentError.questionId == questionId) {
            return currentError;
        } else {
            return null;
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
        Price tempPrice = new Price();

        for (Map.Entry<Pass, Integer> e : passQuantityMap.entrySet()) {
            Integer quantity = e.getValue();
            Pass pass = e.getKey();

            // Prevents NPE
            if (quantity == null || quantity < 0) {
                quantity = 0;
            }

            price += (quantity * pass.getPrice().getValue());
            tempPrice.setCurrency(pass.getPrice().getCurrency());
        }
        tempPrice.setValue(price);
        priceChange.postValue(tempPrice);
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
        public void onError(Error error) {
            state.setValue(State.ERROR);
        }
    };

    public enum State {
        ERROR, LOADING, PASS_SELECTION, QUESTIONS, SUCCESS
    }
}
