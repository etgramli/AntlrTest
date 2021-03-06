package de.etgramlich.dsl.parser.type;

import java.util.List;
import java.util.function.Predicate;

/**
 * Represents a sequence of elements from BNF as Java type.
 */
public final class Sequence implements BnfType {
    /**
     * Elements of the BNF sequence.
     */
    private final List<Element> elements;

    /**
     * Creates a new Sequence with a copy of the provided list.
     * @param elements List of elements, must not be null and not empty.
     */
    public Sequence(final List<Element> elements) {
        if (elements == null || elements.isEmpty()) {
            throw new IllegalArgumentException("Element list must be not null and not empty!");
        }
        this.elements = List.copyOf(elements);
    }

    /**
     * Returns the immutable list of elements.
     * @return List of elements, not null and not empty.
     */
    public List<Element> getElements() {
        return elements;
    }

    @Override
    public String getName() {
        return elements.get(0).getName();
    }

    @Override
    public boolean isTerminal() {
        return elements.stream().filter(Predicate.not(BnfType::isTerminal)).findAny().isEmpty();
    }

    @Override
    public int getNumberOfElements() {
        return elements.stream().mapToInt(BnfType::getNumberOfElements).sum();
    }
}
