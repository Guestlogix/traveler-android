package com.guestlogix.traveleruikit.viewmodels;

import androidx.lifecycle.ViewModel;
import com.guestlogix.traveleruikit.utils.SingleLiveEvent;

public abstract class StatefulViewModel extends ViewModel {

    protected SingleLiveEvent<State> status = new SingleLiveEvent<>();
    public SingleLiveEvent<State> getStatus() {
        return status;
    }

    public enum State {
        SUCCESS,
        ERROR,
        LOADING
    }
}
