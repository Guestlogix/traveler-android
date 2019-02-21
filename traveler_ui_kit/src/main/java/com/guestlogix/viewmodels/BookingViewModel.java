package com.guestlogix.viewmodels;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.guestlogix.travelercorekit.callbacks.FetchPassesCallback;
import com.guestlogix.travelercorekit.error.TravelerError;
import com.guestlogix.travelercorekit.models.*;
import com.guestlogix.travelercorekit.validators.ValidationError;
import com.guestlogix.traveleruikit.forms.descriptors.InputDescriptor;
import com.guestlogix.traveleruikit.forms.descriptors.SpinnerDescriptor;
import com.guestlogix.traveleruikit.forms.descriptors.TextDescriptor;
import com.guestlogix.traveleruikit.forms.utilities.FormType;
import com.guestlogix.traveleruikit.repositories.BookingRepository;
import com.guestlogix.traveleruikit.utils.SingleLiveEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingViewModel extends ViewModel {
    private static final String TAG = "SupplierQuestionsVM";

    private BookingContext bookingContext;
    private BookingForm bookingForm;
    private BookingRepository bookingRepository;
    private Map<Pass, Integer> passQuantityMap;
    private List<BookingForm.BookingFormError> errors;

    private MutableLiveData<List<Pass>> passesLiveData;
    private MutableLiveData<Double> priceChange;
    private MutableLiveData<State> state;
    private SingleLiveEvent<Pair<Integer, Integer>> bookingFormErrors;


    public BookingViewModel() {
        bookingRepository = new BookingRepository();
        passesLiveData = new MutableLiveData<>();
        state = new MutableLiveData<>();
        priceChange = new MutableLiveData<>();
        bookingFormErrors = new SingleLiveEvent<>();
    }

    public void setBookingContext(BookingContext bookingContext) {
        this.bookingContext = bookingContext;
        updatePass(this.bookingContext);
    }

    public LiveData<List<Pass>> getPassesObservable() {
        return passesLiveData;
    }

    public LiveData<Double> getPriceChangeObservable() {
        return priceChange;
    }

    public LiveData<State> getStatus() {
        return state;
    }

    public LiveData<Pair<Integer, Integer>> getBookingFormErrorsObservable() {
        return bookingFormErrors;
    }


    public int getPassQuantity(Pass pass) {
        Integer quantity = passQuantityMap.get(pass);

        if (quantity == null) {
            quantity = 0;
        }

        return quantity;
    }

    public void bookNow() {
        state.postValue(State.LOADING);
        bookingForm = new BookingForm(flattenMap());
        state.postValue(State.QUESTIONS);
    }

    public void updateValueForPass(Pass pass, Integer newQuantity) {
        if (passQuantityMap == null) {
            Log.w(TAG, "updateValueForPass - Passes are not initialized yet");
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

    public void addAnswer(int sectionId, int fieldId, Object value) {
        Question question = bookingForm.getQuestionGroups().get(sectionId).getQuestions().get(fieldId);

        if (question.getType() == QuestionType.STRING) {
            CharSequence cs = (CharSequence) value;
            TextualAnswer answer = new TextualAnswer(cs.toString(), question);
            bookingForm.addAnswer(answer, question);
        } else if (question.getType() == QuestionType.MULTIPLE_CHOICE) {
            MultipleChoiceSelection answer = new MultipleChoiceSelection((Integer) value, question);
            bookingForm.addAnswer(answer, question);
        } else {
            throw new IllegalArgumentException("Unsupported question type");
        }
    }

    public int getSectionCount() {
        return bookingForm.getQuestionGroups().size();
    }

    public int getFieldCount(int sectionId) {
        return bookingForm.getQuestionGroups().get(sectionId).getQuestions().size();
    }

    @NonNull
    public InputDescriptor buildInputDescriptor(int sectionId, int fieldId) {
        Question question = bookingForm.getQuestionGroups().get(sectionId).getQuestions().get(fieldId);

        if (question.getType() == QuestionType.STRING) {
            TextDescriptor t = new TextDescriptor();
            t.hint = question.getTitle();

            return t;
        } else {
            SpinnerDescriptor s = new SpinnerDescriptor();
            List<Choice> choices = (List<Choice>) question.getOptions();
            List<String> options = new ArrayList<>();

            for (Choice c : choices) {
                options.add(c.getValue());
            }

            s.title = question.getTitle();
            s.options = options;

            return s;
        }
    }

    public int getType(int sectionId, int fieldId) {
        QuestionType type = bookingForm.getQuestionGroups().get(sectionId).getQuestions().get(fieldId).getType();

        if (type == QuestionType.STRING) {
            return FormType.TEXT.getValue();
        } else if (type == QuestionType.MULTIPLE_CHOICE) {
            return FormType.SPINNER.getValue();
        }

        return 0;
    }

    @Nullable
    public String getTitle(int sectionId) {
        QuestionGroup qg = bookingForm.getQuestionGroups().get(sectionId);
        return qg.getTitle();
    }

    @Nullable
    public String getDisclaimer(int sectionId) {
        QuestionGroup qg = bookingForm.getQuestionGroups().get(sectionId);
        return qg.getDisclaimer();
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

    public void submitBookingForm() {
        Pair<Integer, Integer> firstError = bookingForm.validate();

        if (firstError != null) {
            // Form has errors
            bookingFormErrors.postValue(firstError);
        } else {
            // SUCCESS
        }
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

    FetchPassesCallback fetchPassesCallback = new FetchPassesCallback() {
        @Override
        public void onSuccess(List<Pass> passes) {
            Log.v(TAG, String.format("Fetched %d passes. Building all mappings", passes.size()));
            passQuantityMap = new HashMap<>();

            // Initialize all quantities to 0.
            for (Pass pass : passes) {
                passQuantityMap.put(pass, 0);
            }

            calculateTotalPrice();
            passesLiveData.postValue(passes);
            state.postValue(State.PASS_SELECTION);
        }

        @Override
        public void onError(TravelerError error) {
            Log.d(TAG, "Error occurred while fetching passes.");
            state.setValue(State.ERROR);
        }
    };

    public Object getFormValue(int sectionId, int fieldId) {
        Question q = bookingForm.getQuestionGroups().get(sectionId).getQuestions().get(fieldId);

        Object value = null;
        if (q.getType() == QuestionType.STRING) {
            TextualAnswer ta = (TextualAnswer) bookingForm.getAnswer(q);

            if (ta != null) {
                value = ta.getValue();
            }
        } else if (q.getType() == QuestionType.MULTIPLE_CHOICE) {
            MultipleChoiceSelection mc = (MultipleChoiceSelection) bookingForm.getAnswer(q);

            if (mc != null) {
                value = mc.getValue();
            }
        }

        return value;
    }

    @Nullable
    public ValidationError getFormError(int sectionId, int fieldId) {
        Question q = bookingForm.getQuestionGroups().get(sectionId).getQuestions().get(fieldId);
        List<BookingForm.BookingFormError> errors = bookingForm.getErrors(q);

        ValidationError e = null;

        if (errors != null && !errors.isEmpty()) {
            e = errors.get(0).error;
        }

        return e;
    }

    public enum State {
        ERROR, LOADING, PASS_SELECTION, QUESTIONS, SUCCESS
    }
}
