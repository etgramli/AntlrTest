package de.etgramlich.antlr.parser.type.rhstype;

import de.etgramlich.antlr.parser.type.BnfType;
import de.etgramlich.antlr.util.StringUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public final class Alternative implements BnfType {
    private final List<Element> elements;

    public Alternative(final Collection<Element> elements) {
        this.elements = List.copyOf(elements);
    }

    public Alternative(final Element element) {
        this.elements = List.of(element);
    }

    @NotNull
    @Contract(pure = true)
    public List<Element> getElements() {
        return elements;
    }

    @Contract(pure = true)
    public boolean hasOneElementInSequence() {
        return elements.size() == 1;
    }

    @Override
    public String getName() {
        return elements.isEmpty() ? StringUtil.EMPTY : elements.get(0).getName();
    }

    @Override
    public boolean isTerminal() {
        return elements.stream().filter(element -> !element.isTerminal()).findAny().isEmpty();
    }

    @Override
    public List<String> getNonTerminalDependants() {
        if (isTerminal()) return Collections.emptyList();
        return elements.stream().flatMap(e -> e.getNonTerminalDependants().stream()).collect(Collectors.toList());
    }

    @Override
    public void removeNonTerminals() {
        // ToDo
    }
}
