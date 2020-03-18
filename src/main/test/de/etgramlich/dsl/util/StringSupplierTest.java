package de.etgramlich.dsl.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringSupplierTest {

    @Test
    void test_constructorWithNullArgument_throwsException() {
        final String prefix = null;

        assertThrows(IllegalArgumentException.class, () -> new StringSupplier(prefix));
    }

    @Test
    void test_constructorWithBlankArgument_throwsException() {
        final String prefix = "   \t";

        assertThrows(IllegalArgumentException.class, () -> new StringSupplier(prefix));
    }

    @Test
    void get() {
        final String prefix = "ABC";
        final StringSupplier supplier = new StringSupplier(prefix);

        assertEquals("ABC0", supplier.get());
        assertEquals("ABC1", supplier.get());
        for (int i = 2; i < 100; ++i) {
            assertEquals("ABC" + i, supplier.get());
        }
    }
}