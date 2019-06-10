package com.guestlogix.travelercorekit.utilities;

public class Assertion {
    /**
     * Ensures that {@code condition} is true, throws {@link AssertionException} otherwise.
     * @param condition to evaluate
     * @throws AssertionException if condition is false.
     */
    public static void eval(boolean condition) throws AssertionException {
        if (!condition) {
            throw new AssertionException();
        }
    }

    /**
     * Ensures that {@code condition} is true, throws {@link AssertionException} with message otherwise.
     * @param condition to evaluate
     * @param message to set as {@code AssertionException} message.
     * @throws AssertionException if condition fails.
     */
    public static void eval(boolean condition, String message) throws AssertionException {
        if (!condition) {
            throw new AssertionException(message);
        }
    }
}