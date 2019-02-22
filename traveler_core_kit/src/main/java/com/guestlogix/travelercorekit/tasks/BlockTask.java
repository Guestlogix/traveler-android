package com.guestlogix.travelercorekit.tasks;

public abstract class BlockTask extends Task {
    public void execute() {
        main();
        finish();
    }

    protected abstract void main();
}
