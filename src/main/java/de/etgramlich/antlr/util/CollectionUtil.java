package de.etgramlich.antlr.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CollectionUtil {
    @Contract(pure = true)
    private CollectionUtil() {}

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
