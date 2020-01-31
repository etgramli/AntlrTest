package de.etgramlich.parser.type;

import java.util.Collections;
import java.util.List;

public final class NonTerminal extends TextElement {
    public NonTerminal(final String name) {
        super(name);
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public int getNumberOfElements() {
        return 1;
    }

    @Override
    public List<String> getNonTerminalDependants() {
        return Collections.emptyList();
    }
}
