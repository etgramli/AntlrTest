package de.etgramlich.dsl.parser.type.text;

/**
 * Represents a type from BNF as Java type.
 */
public final class Type extends TextElement {
    /**
     * Creates new Type from String.
     * @param name String, must not be blank.
     */
    public Type(final String name) {
        super(name);
    }

    @Override
    public boolean isTerminal() {
        return true;
    }

    @Override
    public String toString() {
        return "Type(" + getName() + ")";
    }
}
