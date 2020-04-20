package de.etgramlich.dsl.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class CollectionUtil {
    private CollectionUtil() { }

    /**
     * Determines whether the passed collection contains duplicate elements.
     * @param collection A collection of type T, must not be null.
     * @param <T> A type.
     * @return True if the collection contains duplicate elements.
     */
    public static <T> boolean containsDuplicates(final Collection<T> collection) {
        if (collection instanceof Set) {
            return false;
        }
        final Set<T> set = new HashSet<>();
        for (T element : collection) {
            if (!set.add(element)) {
                return true;
            }
        }
        return false;
    }
}
