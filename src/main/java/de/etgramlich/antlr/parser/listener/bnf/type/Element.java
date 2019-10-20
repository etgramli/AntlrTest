package de.etgramlich.antlr.parser.listener.bnf.type;

import de.etgramlich.antlr.parser.listener.bnf.type.repetition.Repetition;
import de.etgramlich.antlr.parser.listener.bnf.type.terminal.ID;
import org.jetbrains.annotations.Contract;

import java.util.Objects;

public class Element {
    private ID id;
    private Repetition repetition;

    @Contract(pure = true)
    public Element(final ID id) {
        this.id = id;
        repetition = null;
    }

    @Contract(pure = true)
    public Element(final Repetition repetition) {
        id = null;
        this.repetition = repetition;
    }


    public ID getId() {
        return id;
    }

    public Repetition getRepetition() {
        return repetition;
    }


    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Element element = (Element) o;

        if (!Objects.equals(id, element.id)) return false;
        return Objects.equals(repetition, element.repetition);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (repetition != null ? repetition.hashCode() : 0);
        return result;
    }
}
