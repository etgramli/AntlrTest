package de.etgramlich.dsl.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
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

    /**
     * Generates a markdown representation of the passed map and the strings as headlines for the columns.
     * @param map Map of String to String, must not be null.
     * @param firstColumn String, must not be blank.
     * @param secondColumn String, must not be blank.
     * @return String, not empty.
     */
    public static String toMarkdownTable(final Map<String, String> map,
                                         final String firstColumn,
                                         final String secondColumn) {
        if (map == null) {
            throw new IllegalArgumentException("Map must not be null!");
        }
        if (StringUtil.isBlank(firstColumn)) {
            throw new IllegalArgumentException("First column must not be blank!");
        }
        if (StringUtil.isBlank(secondColumn)) {
            throw new IllegalArgumentException("Second column must no be blank!");
        }
        final String lineSeparator = " | ";
        final StringBuilder sb = new StringBuilder(firstColumn)
                .append(lineSeparator)
                .append(secondColumn)
                .append(System.lineSeparator())
                .append("------|------")
                .append(System.lineSeparator());
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey()).append(lineSeparator).append(entry.getValue()).append(System.lineSeparator());
        }
        return sb.toString();
    }
}
