package de.etgramlich.dsl.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

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

    /**
     * Converts the set to a single String, elements separated by commas.
     * @param stringSet Set of Strings, must not be null.
     * @return String, not null.
     */
    public static String asString(final Set<String> stringSet) {
        if (stringSet == null) {
            throw new IllegalArgumentException("String set must not be null!");
        }
        final StringBuilder sb = new StringBuilder();

        for (final Iterator<String> iter = stringSet.iterator(); iter.hasNext();) {
            sb.append(iter.next());
            if (iter.hasNext()) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
