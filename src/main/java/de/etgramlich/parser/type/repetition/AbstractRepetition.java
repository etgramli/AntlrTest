package de.etgramlich.parser.type.repetition;

import de.etgramlich.parser.type.Alternatives;
import de.etgramlich.parser.type.BnfType;
import de.etgramlich.parser.type.Element;
import de.etgramlich.util.StringUtil;

/**
 * Represents an element containing alternatives from BNF as Java type.
 */
public abstract class AbstractRepetition implements Element, BnfType {
    /**
     * Alternatives BNF element representing this AbstractRepetition.
     */
    private final Alternatives alternatives;

    AbstractRepetition(final Alternatives alternatives) {
        if (alternatives == null) {
            throw new IllegalArgumentException("Alternatives must not be null!");
        }
        this.alternatives = alternatives;
    }

    /**
     * Returns alternatives object.
     * @return Alternatives, not null.
     */
    public Alternatives getAlternatives() {
        return alternatives;
    }

    @Override
    public final boolean isTerminal() {
        return alternatives.getSequences().stream()
                .filter(alternative -> !alternative.isTerminal()).findAny().isEmpty();
    }

    @Override
    public final int getNumberOfElements() {
        return alternatives.getNumberOfElements();
    }

    @Override
    public final String getName() {
        return alternatives.getSequences().isEmpty() ? StringUtil.EMPTY : alternatives.getSequences().get(0).getName();
    }
}
