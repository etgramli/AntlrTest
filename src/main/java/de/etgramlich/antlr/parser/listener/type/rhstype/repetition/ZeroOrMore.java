package de.etgramlich.antlr.parser.listener.type.rhstype.repetition;

import de.etgramlich.antlr.parser.listener.type.rhstype.Alternative;

import java.util.Collection;

public final class ZeroOrMore extends AbstractRepetition {
    public ZeroOrMore(Collection<Alternative> alternatives) {
        super(alternatives);
    }
}
