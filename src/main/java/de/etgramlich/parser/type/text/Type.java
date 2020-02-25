package de.etgramlich.parser.type.text;

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
}
