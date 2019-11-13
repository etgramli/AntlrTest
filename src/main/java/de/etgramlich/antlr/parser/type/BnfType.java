package de.etgramlich.antlr.parser.type;

public interface BnfType {
    default boolean isTerminal() {
        return false;
    }
    default boolean isRepetition() {
        return false;
    }
    default boolean isAlternative() {
        return false;
    }
}
