package de.etgramlich.antlr.parser.listener.bnf.type;

import de.etgramlich.antlr.util.BnfElement;
import de.etgramlich.antlr.util.BnfTypeVisitor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public final class Alternative implements BnfElement {
    private List<Element> elements;

    public Alternative(final Collection<Element> elements) {
        this.elements = List.copyOf(elements);
    }

    @NotNull
    @Contract(pure = true)
    public List<Element> getElements() {
        return elements;
    }

    @Override
    public void accept(@NotNull BnfTypeVisitor visitor) {
        elements.forEach(element -> accept(visitor));
        visitor.visit(this);
    }
}
