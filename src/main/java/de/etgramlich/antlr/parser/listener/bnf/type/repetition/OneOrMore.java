package de.etgramlich.antlr.parser.listener.bnf.type.repetition;

import de.etgramlich.antlr.parser.listener.bnf.type.Alternative;

import java.util.Collection;

public final class OneOrMore extends AbstractRepetition {
    protected OneOrMore(Collection<Alternative> alternatives) {
        super(alternatives);
        if (alternatives.size() == 0) {
            throw new IllegalArgumentException("OneOrMore must have at least 1 Alternative!");
        }
    }
}
