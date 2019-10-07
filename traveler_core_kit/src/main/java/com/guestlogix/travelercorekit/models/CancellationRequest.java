package com.guestlogix.travelercorekit.models;

public class CancellationRequest {
    private CancellationQuote cancellationQuote;
    private CancellationReason cancellationReason;
    private String explanation;

    public CancellationRequest(CancellationQuote cancellationQuote,
                               CancellationReason cancellationReason,
                               String explanation) {
        this.cancellationQuote = cancellationQuote;
        this.cancellationReason = cancellationReason;
        this.explanation = explanation;
    }

    public CancellationQuote getCancellationQuote() {
        return cancellationQuote;
    }

    public CancellationReason getCancellationReason() {
        return cancellationReason;
    }

    public String getExplanation() {
        return explanation;
    }
}
