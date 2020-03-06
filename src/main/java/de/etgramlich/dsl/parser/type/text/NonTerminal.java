package de.etgramlich.dsl.parser.type.text;

/**
 * Represents a non terminal from BNF as Java type.
 */
public final class NonTerminal extends TextElement {
    /**
     * Creates mew NonTerminal from String.
     * @param name String, must not be blank.
     */
    public NonTerminal(final String name) {
        super(name);
    }

    @Override
    public boolean isTerminal() {
        return false;
    }
}
