package de.etgramlich.parser.type.repetition;

import de.etgramlich.parser.type.Alternatives;
import de.etgramlich.parser.type.BnfType;
import de.etgramlich.parser.type.Element;
import de.etgramlich.util.StringUtil;

import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * Determines if this is a repetition.
     * @return True if this is a repetition.
     */
    public abstract boolean isRepetition();

    /**
     * Determines if this is optional.
     * @return True if this is optional.
     */
    public abstract boolean isOptional();

    /**
     * Determines if this is a precedence.
     * @return True if this a precedence.
     */
    public abstract boolean isPrecedence();

    /**
     * Determins if this contains only one sequence element.
     * @return Trur if it is not a real alternative and contains only one sequence element.
     */
    public boolean hasOnlyOneAlternative() {
        return alternatives.getSequences().size() == 1;
    }

    @Override
    public final boolean isTerminal() {
        return alternatives.getSequences().stream()
                .filter(alternative -> !alternative.isTerminal()).findAny().isEmpty();
    }

    @Override
    public final List<String> getNonTerminalDependants() {
        return getAlternatives().getSequences().stream()
                .flatMap(a -> a.getNonTerminalDependants().stream()).collect(Collectors.toList());
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
