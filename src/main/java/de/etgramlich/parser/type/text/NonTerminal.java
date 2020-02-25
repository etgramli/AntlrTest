package de.etgramlich.parser.type.text;

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
