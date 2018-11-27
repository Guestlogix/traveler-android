package com.guestlogix.network;

import java.io.InputStream;

public interface NetworkResponseHandler {
    void onSuccess(InputStream is);
    void onError(Error error);
}
