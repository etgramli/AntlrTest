package de.etgramlich.dsl.util;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class StringSupplierTest {
    private static final String PREFIX = "ABC";

    @Test
    void test_constructorWithNullArgument_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new StringSupplier(null));
    }

    @Test
    void test_constructorWithBlankArgument_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new StringSupplier("   \t"));
    }

    @Test
    void test_get_continuousCalls() {
        final int numberOfTests = 1_000;
        final StringSupplier supplier = new StringSupplier(PREFIX);

        for (long i = 0; i < numberOfTests; ++i) {
            assertEquals(PREFIX + i, supplier.get());
        }
    }

    @Test
    void test_get_overflow() {
        final StringSupplier supplier = new StringSupplier(PREFIX);

        try {
            final Field counterField = supplier.getClass().getDeclaredField("counter");
            counterField.setAccessible(true);
            counterField.setLong(supplier, Long.MAX_VALUE);
            assertEquals(PREFIX + Long.MAX_VALUE, supplier.get());
            assertEquals(PREFIX + Long.MIN_VALUE, supplier.get());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail(e.getMessage());
        }
    }
}