package de.etgramlich.antlr.parser.type;

public interface BnfType {
    String getName();

    default boolean isTerminal() {
        return false;
    }
}
