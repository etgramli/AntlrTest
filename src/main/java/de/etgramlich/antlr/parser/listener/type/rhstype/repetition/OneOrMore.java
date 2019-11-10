package de.etgramlich.antlr.parser.listener.type.rhstype.repetition;

import de.etgramlich.antlr.parser.listener.type.rhstype.Alternative;

import java.util.Collection;

public final class OneOrMore extends AbstractRepetition {
    public OneOrMore(Collection<Alternative> alternatives) {
        super(alternatives);
        if (alternatives.size() == 0) {
            throw new IllegalArgumentException("OneOrMore must have at least 1 Alternative!");
        }
    }
}
