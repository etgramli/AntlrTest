package de.etgramlich.parser.type;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public final class Sequence implements BnfType {
    private final List<Element> elements;

    public Sequence(final Collection<Element> elements) {
        if (elements == null || elements.isEmpty()) {
            throw new IllegalArgumentException("Element list must be not null and not empty!");
        }
        this.elements = List.copyOf(elements);
    }

    public List<Element> getElements() {
        return elements;
    }

    @Override
    public String getName() {
        return elements.get(0).getName();
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public List<String> getNonTerminalDependants() {
        return elements.stream().flatMap(element -> element.getNonTerminalDependants().stream()).collect(Collectors.toList());
    }
}
