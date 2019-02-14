package com.guestlogix.viewmodels;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.guestlogix.travelercorekit.callbacks.FetchPassesCallback;
import com.guestlogix.travelercorekit.error.TravelerError;
import com.guestlogix.travelercorekit.models.BookingContext;
import com.guestlogix.travelercorekit.models.Pass;
import com.guestlogix.traveleruikit.repositories.SupplierQuestionsRepository;

import java.util.List;

public class SupplierQuestionsViewModel extends StatefulViewModel {
    private static final String TAG = "SupplierQuestionsVM";

    private BookingContext bookingContext;
    private SupplierQuestionsRepository supplierQuestionsRepository;
    private MutableLiveData<List<Pass>> passesLiveData;

    public SupplierQuestionsViewModel() {
        supplierQuestionsRepository = new SupplierQuestionsRepository();
        passesLiveData = new MutableLiveData<>();
    }

    public void setBookingContext(BookingContext bookingContext) {
        this.bookingContext = bookingContext;
        updatePass(this.bookingContext);
    }

    private void updatePass(BookingContext bookingContext) {
        status.setValue(State.LOADING);
        supplierQuestionsRepository.fetchPasses(bookingContext, fetchPassesCallback);
    }

    FetchPassesCallback fetchPassesCallback = new FetchPassesCallback() {
        @Override
        public void onSuccess(List<Pass> pass) {
            passesLiveData.postValue(pass);
            status.postValue(State.SUCCESS);
        }

        @Override
        public void onError(TravelerError error) {
            Log.d(TAG, "Error occurred while fetching passes.");
            status.setValue(State.ERROR);
        }
    };
}
