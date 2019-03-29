package com.guestlogix.traveleruikit.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import com.guestlogix.traveleruikit.utils.SingleLiveEvent;

public abstract class StatefulViewModel extends AndroidViewModel {

    protected SingleLiveEvent<State> status = new SingleLiveEvent<>();

    public StatefulViewModel(@NonNull Application application) {
        super(application);
    }

    public SingleLiveEvent<State> getStatus() {
        return status;
    }

    public enum State {
        SUCCESS,
        ERROR,
        LOADING
    }
}
