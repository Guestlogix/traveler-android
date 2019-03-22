package com.guestlogix.traveleruikit.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.guestlogix.travelercorekit.models.Receipt;

public class OrderConfirmationViewModel extends ViewModel {

    private MutableLiveData<Receipt> receipt = new MutableLiveData<>();

    public LiveData<Receipt> getObservableReceipt() {
        return receipt;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt.setValue(receipt);
    }
}
