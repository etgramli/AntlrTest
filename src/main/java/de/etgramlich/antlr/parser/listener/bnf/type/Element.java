package de.etgramlich.antlr.parser.listener.bnf.type;

import de.etgramlich.antlr.parser.listener.bnf.type.terminal.ID;
import org.jetbrains.annotations.Contract;

public final class Element implements BnfType {
    private final ID id;
    private final Alternatives alternatives;

    @Contract(pure = true)
    public Element(final ID id) {
        this.id = id;
        alternatives = null;
    }

    @Contract(pure = true)
    public Element(final Alternatives alternatives) {
        id = null;
        this.alternatives = alternatives;
    }


    @Contract(pure = true)
    public ID getId() {
        return id;
    }

    @Contract(pure = true)
    public Alternatives getAlternatives() {
        return alternatives;
    }
}
