package de.etgramlich.dsl.util;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Contains misc functions for Collections.
 */
public final class CollectionUtil {
    private CollectionUtil() { }

    /**
     * Creates a list of the objects passed as varargs. Never returns null,
     * instead returns an empty list when null is passed as array, or a List containing
     * a null object when null is passes as Object.
     *
     * @param items (Array) containing the elements to convert to list.
     * @param <T> The type of objects passed.
     * @return A list, that might be empty or contain null objects.
     */
    @SafeVarargs
    public static <T> List<T> toList(final T... items) {
        return new ArrayList<>(items != null ? Arrays.asList(items) : Collections.emptyList());
    }
}
