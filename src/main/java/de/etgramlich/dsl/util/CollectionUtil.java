package de.etgramlich.dsl.util;

import java.util.Collection;
import java.util.Set;

public final class CollectionUtil {
    private CollectionUtil() { }

    /**
     * Determines whether the passed collection contains duplicate elements.
     * @param collection A collection of type T, must not be null.
     * @param <T> A type.
     * @return True if the collection contains duplicate elements.
     */
    public static <T> boolean hasDuplicates(final Collection<T> collection) {
        final Set<T> set = Set.copyOf(collection);
        return set.size() < collection.size();
    }
}
