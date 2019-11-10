package de.etgramlich.antlr.parser.listener.type.rhstype.repetition;

import de.etgramlich.antlr.parser.listener.type.rhstype.Alternative;

import java.util.Collection;

public final class Optional extends AbstractRepetition {
    public Optional(Collection<Alternative> alternatives) {
        super(alternatives);
        if (alternatives.size() > 1) {
            throw new IllegalArgumentException("Optional argument has more than one Alternatives! (must be 0 or 1)");
        }
    }
}
