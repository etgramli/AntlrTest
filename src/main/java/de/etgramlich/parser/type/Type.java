package de.etgramlich.parser.type;

public final class Type extends TextElement {
    public Type(String name) {
        super(name);
    }

    @Override
    public boolean isTerminal() {
        return true;
    }
}
