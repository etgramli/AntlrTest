package de.etgramlich.parser.type.repetition;

import de.etgramlich.parser.type.Alternatives;

public final class Optional extends AbstractRepetition {
    /**
     * Creates new Optional object from Alternatives.
     * @param alternatives Alternatives, must not be null.
     */
    public Optional(final Alternatives alternatives) {
        super(alternatives);
        if (alternatives.getSequences().size() > 1) {
            throw new IllegalArgumentException("Optional argument has more than one Alternatives! (must be 0 or 1)");
        }
    }

    @Override
    public boolean isRepetition() {
        return false;
    }

    @Override
    public boolean isOptional() {
        return true;
    }

    @Override
    public boolean isPrecedence() {
        return false;
    }
}
