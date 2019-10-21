package de.etgramlich.antlr.parser.listener.bnf.type;

import de.etgramlich.antlr.parser.listener.bnf.type.terminal.ID;
import org.jetbrains.annotations.Contract;

import java.util.Objects;

public class Element {
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


    public ID getId() {
        return id;
    }

    public Alternatives getAlternatives() {
        return alternatives;
    }


    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Element element = (Element) o;

        if (!Objects.equals(id, element.id)) return false;
        return Objects.equals(alternatives, element.alternatives);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (alternatives != null ? alternatives.hashCode() : 0);
        return result;
    }
}
