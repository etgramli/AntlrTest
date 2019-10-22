package de.etgramlich.antlr.parser.listener.bnf.type.terminal;

import de.etgramlich.antlr.parser.listener.bnf.type.BnfType;

public interface ID extends BnfType {
    String getText();

    @Override
    default boolean isTerminal() {
        return true;
    }
}
