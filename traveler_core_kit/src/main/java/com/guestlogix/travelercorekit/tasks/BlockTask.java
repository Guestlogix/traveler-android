package com.guestlogix.travelercorekit.tasks;

import com.guestlogix.travelercorekit.utilities.Task;

public abstract class BlockTask extends Task {
    public void execute() {
        main();
        finish();
    }

    protected abstract void main();
}
