package com.guestlogix.travelercorekit.utilities;

public class Assertion {
    public static void eval(boolean condition) throws AssertionException {
        if (!condition) {
            // TODO: make better assertion exceptions
            throw new AssertionException();
        }
    }
}
