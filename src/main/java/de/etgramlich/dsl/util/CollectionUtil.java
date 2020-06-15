package de.etgramlich.dsl.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toUnmodifiableList;
import static java.util.stream.Collectors.toUnmodifiableSet;

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
     * Converts an array of Map.Entries to an immutable Map.
     * @param entries Array of entries, may be null or empty.
     * @param <K> Type of the keys.
     * @param <V> Type of the values.
     * @return Map, not null, may be empty.
     */
    public static <K, V> Map<K, V> asMap(final Map.Entry<K, V>... entries) {
        if (entries == null || entries.length == 0) {
            return Collections.emptyMap();
        }
        return Arrays.stream(entries).collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Converts an Array of elements to an unmodifiable List.
     * @param elements Array of elements, may be null or empty.
     * @param <T> Type of the elements.
     * @return List, not null, may be empty.
     */
    public static <T> List<T> asList(final T... elements) {
        if (elements == null || elements.length == 0) {
            return Collections.emptyList();
        }
        return Arrays.stream(elements).collect(toUnmodifiableList());
    }

    /**
     * Converts an Array of elements to an unmodifiable Set.
     * @param elements Array of elements, may be null or empty.
     * @param <T> Type of the elements.
     * @return Set, not null, may be empty.
     */
    public static <T> Set<T> asSet(final T... elements) {
        if (elements == null || elements.length == 0) {
            return Collections.emptySet();
        }
        return Arrays.stream(elements).collect(toUnmodifiableSet());
    }

    /**
     * Concatenate strings of the set with the passed delimiter between two strings.
     * @param delimiter Char used to separate two strings.
     * @param strings Set of Strings, may be null or empty.
     * @return String, not null, may be empty.
     */
    public static String join(final char delimiter, final Set<String> strings) {
        return join(String.valueOf(delimiter), strings);
    }

    /**
     * Concatenate strings of the set with the passed delimiter between two strings.
     * @param delimiter Char sequence, must not be null nor be blank.
     * @param strings Set of String, may be null or empty.
     * @return String, not null, may be empty.
     */
    public static String join(final CharSequence delimiter, final Set<String> strings) {
        if (delimiter == null || StringUtil.isBlank(delimiter)) {
            throw new IllegalArgumentException("Delimiter must not be blank!)");
        }
        if (strings == null || strings.isEmpty()) {
            return StringUtil.EMPTY;
        }
        final StringBuilder sb = new StringBuilder();
        for (Iterator<String> string = strings.iterator(); string.hasNext();) {
            sb.append(string.next());
            if (string.hasNext()) {
                sb.append(delimiter);
            }
        }
        return sb.toString();
    }

    /**
     * Concatenate the string representation of the objects in the collection using the delimiter.
     * @param delimiter Character between two elements.
     * @param collection Collection of elements, may be mull or empty.
     * @param <T> Some type overriding toString().
     * @return String, not null.
     */
    public static <T> String join(final char delimiter, final Collection<T> collection) {
        return join(String.valueOf(delimiter), collection);
    }

    /**
     * Concatenate the string representation of the objects (call to toString()) using the delimiter.
     * @param delimiter CharSequence, must not be blank.
     * @param collection Collection of elements, may be null or empty.
     * @param <T> Some type, that should override toString().
     * @return String, may be empty, is not null.
     */
    public static <T> String join(final CharSequence delimiter, final Collection<T> collection) {
        if (StringUtil.isBlank(delimiter)) {
            throw new IllegalArgumentException("Delimiter must not be blank!");
        }
        if (collection == null || collection.isEmpty()) {
            return StringUtil.EMPTY;
        }
        final StringBuilder sb = new StringBuilder();
        for (Iterator<T> iterator = collection.iterator(); iterator.hasNext();) {
            sb.append(iterator.next().toString());
            if (iterator.hasNext()) {
                sb.append(delimiter);
            }
        }
        return sb.toString();
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
        final String head = firstColumn + lineSeparator + secondColumn + System.lineSeparator();
        return head + "------|------" + System.lineSeparator() + appendToLines(map, lineSeparator, StringUtil.EMPTY);
    }

    /**
     * Generates a LaTeX representation of the passed map as tabular with the strings as headlines for the columns.
     * @param map Map of String to String, must not be null.
     * @param firstColumn String, must not be blank.
     * @param secondColumn String, must not be blank.
     * @return String, not null.
     */
    public static String toLaTeXTable(final Map<String, String> map,
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
        final String lineSeparator = " & ";
        final String latexLineEnd = "\\\\";
        final String tabular = "\\begin{tabular}{ll}" + System.lineSeparator();
        final String headLines = "\\textbf{" + firstColumn + "}" + lineSeparator + "\\textbf{" + secondColumn + "}";
        return tabular + headLines + latexLineEnd + "\\hline" + System.lineSeparator()
                + appendToLines(map, lineSeparator, latexLineEnd);
    }

    private static String appendToLines(final Map<String, String> map, final String separator, final String endLine) {
        final StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey())
                    .append(separator)
                    .append(entry.getValue())
                    .append(endLine)
                    .append(System.lineSeparator());
        }
        return sb.toString();
    }
}
