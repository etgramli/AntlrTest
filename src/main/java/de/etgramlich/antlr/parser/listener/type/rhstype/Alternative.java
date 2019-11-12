package de.etgramlich.antlr.parser.listener.type.rhstype;

import de.etgramlich.antlr.parser.listener.type.BnfType;
import de.etgramlich.antlr.util.visitor.BnfElement;
import de.etgramlich.antlr.util.visitor.BnfTypeVisitor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class Alternative implements BnfType, BnfElement {
    private final List<Element> elements;

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
        elements.forEach(element -> element.accept(visitor));
        visitor.visit(this);
    }

    @Override
    public boolean isAlternative() {
        return true;
    }
}
