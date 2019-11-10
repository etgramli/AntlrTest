package de.etgramlich.antlr.parser.listener.type;

public interface BnfType {
    default boolean isTerminal() {
        return false;
    }
}
