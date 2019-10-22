package de.etgramlich.antlr.parser.listener.bnf.type;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class Alternative implements BnfType {
    private final List<Element> elements;

    public Alternative(final Collection<Element> elements) {
        this.elements = List.copyOf(elements);
    }

    @NotNull
    @Contract(pure = true)
    public List<Element> getElements() {
        return elements;
    }

}
