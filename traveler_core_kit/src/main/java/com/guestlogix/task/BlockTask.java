package com.guestlogix.task;

public class BlockTask extends Task {
    public void execute() {
        main();
        finish();
    }

    public void main() {
        // Overridable
    }
}
