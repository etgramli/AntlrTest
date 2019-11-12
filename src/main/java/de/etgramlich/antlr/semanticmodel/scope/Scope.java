package de.etgramlich.antlr.semanticmodel.scope;

public interface Scope {
    String getName();

    // ToDo: Can not "accept" something, must be name from the provided EBNF grammar
    Scope accept(final String terminal);
}
