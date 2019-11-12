package de.etgramlich.antlr.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CollectionUtil {
    @Contract(pure = true)
    private CollectionUtil() {}

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
    @NotNull
    @Contract("!null -> new")
    public static <T> List<T> toList(T... items) {
        if (items == null) {
            return new ArrayList<>();
        } else {
            return new ArrayList<>(Arrays.asList(items));
        }
    }
}
