package de.etgramlich.dsl.util;

public final class IntUtil {
    private IntUtil() { }

    /**
     * Determines whether the passed long is an even number.
     * @param number Long.
     * @return True if number is even.
     */
    public static boolean isEven(final long number) {
        return (number & 1L) == 0L;
    }

    /**
     * Determines whether the passed long is an odd number.
     * @param number Long.
     * @return True if number is odd.
     */
    public static boolean isOdd(final long number) {
        return !isEven(number);
    }

    /**
     * Determines whether the passed long is an even number.
     * @param number Int.
     * @return True if number is even.
     */
    public static boolean isEven(final int number) {
        return (number & 1) == 0;
    }

    /**
     * Determines whether the passed long is an odd number.
     * @param number Int.
     * @return True if number is odd.
     */
    public static boolean isOdd(final int number) {
        return !isEven(number);
    }

    /**
     * Determines whether the passed long is an even number.
     * @param number Short.
     * @return True if number is even.
     */
    public static boolean isEven(final short number) {
        return (number & 1) == 0;
    }

    /**
     * Determines whether the passed long is an odd number.
     * @param number Short.
     * @return True if number is odd.
     */
    public static boolean isOdd(final short number) {
        return !isEven(number);
    }
}
