package de.etgramlich.parser.type;

public final class Keyword extends TextElement {
    public Keyword(final String name) {
        super(name);
    }

    @Override
    public boolean isTerminal() {
        return true;
    }
}
