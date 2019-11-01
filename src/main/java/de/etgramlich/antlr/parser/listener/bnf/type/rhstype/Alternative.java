package de.etgramlich.antlr.parser.listener.bnf.type.rhstype;

import de.etgramlich.antlr.parser.listener.bnf.type.BnfType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class Alternative implements BnfType, RhsType {
    private final List<Element> elements;

    public Alternative(final Collection<Element> elements) {
        this.elements = List.copyOf(elements);
    }

    @NotNull
    @Contract(pure = true)
    public List<Element> getElements() {
        return elements;
    }

    @Contract(pure = true)
    @Override
    public boolean isLeaf() {
        return false;
    }

    @NotNull
    @Contract(pure = true)
    public List<RhsType> getChildren() {
        return Collections.unmodifiableList(elements);
    }
}
