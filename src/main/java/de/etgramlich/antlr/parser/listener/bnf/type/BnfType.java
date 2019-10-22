package de.etgramlich.antlr.parser.listener.bnf.type;

public interface BnfType {
    default boolean isTerminal() {
        return false;
    }
}
