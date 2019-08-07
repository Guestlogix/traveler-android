package com.guestlogix.travelercorekit.callbacks;

import com.guestlogix.travelercorekit.models.CancellationQuote;

public interface CancellationQuoteCallback {
    void onCancellationQuoteSuccess(CancellationQuote quote);
    void onCancellationQuoteError(Error error);
}
