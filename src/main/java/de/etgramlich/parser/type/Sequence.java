package de.etgramlich.parser.type;

import java.util.List;
import java.util.stream.Collectors;

public final class Sequence implements BnfType {
    private final List<Element> elements;

    public Sequence(final List<Element> elements) {
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
        return elements.stream().filter(element -> !element.isTerminal()).findAny().isEmpty();
    }

    @Override
    public int getNumberOfElements() {
        int counter = 0;
        for (Element element : elements) {
            counter += element.getNumberOfElements();
        }
        return counter;
    }

    @Override
    public List<String> getNonTerminalDependants() {
        return elements.stream().flatMap(element -> element.getNonTerminalDependants().stream()).collect(Collectors.toList());
    }
}
