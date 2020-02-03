package de.etgramlich.parser.type.text;

public final class Type extends TextElement {
    public Type(String name) {
        super(name);
    }

    @Override
    public boolean isTerminal() {
        return true;
    }
}
