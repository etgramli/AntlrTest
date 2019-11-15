package de.etgramlich.antlr.parser.type;

public interface BnfType {
    default boolean isTerminal() {
        return false;
    }
}
