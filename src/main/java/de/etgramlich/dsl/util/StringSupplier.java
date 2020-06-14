package de.etgramlich.dsl.util;

import org.apache.commons.lang3.StringUtils;

import java.util.function.Supplier;

/**
 * Generates new strings by appending a number to a given prefix.
 */
public final class StringSupplier implements Supplier<String> {

    /**
     * Prefix for the returned strings.
     */
    private final String prefix;

    /**
     * Counter to be incremented to create new strings.
     */
    private long counter;

    /**
     * Creates a new StringSupplier with the specified prefix.
     * @param prefix String, must not be null and not be empty.
     */
    public StringSupplier(final String prefix) {
        if (StringUtils.isBlank(prefix)) {
            throw new IllegalArgumentException("Prefix must not be null and not be blank");
        }
        this.prefix = prefix;
        this.counter = 0;
    }

    @Override
    public String get() {
        return prefix + counter++;
    }
}
