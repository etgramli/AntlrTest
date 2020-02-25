package de.etgramlich.parser.type.text;

public final class Keyword extends TextElement {
    /**
     * Creates new Keyword from String.
     * @param name String, must not be blank.
     */
    public Keyword(final String name) {
        super(name);
    }

    @Override
    public boolean isTerminal() {
        return true;
    }
}
