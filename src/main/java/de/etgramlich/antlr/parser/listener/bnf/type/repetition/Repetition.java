package de.etgramlich.antlr.parser.listener.bnf.type.repetition;

import de.etgramlich.antlr.parser.listener.bnf.type.Alternative;

import java.util.List;

public interface Repetition {
    List<Alternative> getAlternatives();
}
