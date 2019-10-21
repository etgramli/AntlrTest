package de.etgramlich.antlr.parser.listener.bnf.type;

import de.etgramlich.antlr.parser.listener.bnf.type.terminal.ID;
import de.etgramlich.antlr.util.BnfElement;
import de.etgramlich.antlr.util.BnfTypeVisitor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class Element implements BnfElement {
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

    @Override
    public void accept(@NotNull BnfTypeVisitor visitor) {
        if (id != null) id.accept(visitor);
        if (alternatives != null) alternatives.accept(visitor);

        visitor.visit(this);
    }
}
