package de.etgramlich.antlr.semanticmodel.scope;

public interface Scope {
    String getName();
    Scope accept(final String terminal);
}
