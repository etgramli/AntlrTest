package de.etgramlich.dsl.parser.type.text;

/**
 * Representing a keyword in BNF as Java type.
 */
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
