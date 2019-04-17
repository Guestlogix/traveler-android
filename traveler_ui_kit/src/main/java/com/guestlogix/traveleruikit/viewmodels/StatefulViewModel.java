package com.guestlogix.traveleruikit.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public abstract class StatefulViewModel extends AndroidViewModel {

    protected MutableLiveData<State> status = new MutableLiveData<>();

    public StatefulViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<State> getStatus() {
        return status;
    }

    public enum State {
        SUCCESS,
        ERROR,
        LOADING
    }
}
