package de.etgramlich.antlr.parser.type.rhstype;

import de.etgramlich.antlr.parser.type.BnfType;
import de.etgramlich.antlr.util.StringUtil;
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

    @Contract(pure = true)
    public boolean hasOneElementInSequence() {
        return elements.size() == 1;
    }

    @Override
    public void accept(@NotNull BnfTypeVisitor visitor) {
        elements.forEach(element -> element.accept(visitor));
        visitor.visit(this);
    }

    @Override
    public String getName() {
        return elements.isEmpty() ? StringUtil.EMPTY : elements.get(0).getName();
    }

    @Override
    public boolean isTerminal() {
        return elements.stream().filter(element -> !element.isTerminal()).findAny().isEmpty();
    }

    @Override
    public List<String> getNonTerminalDependants() {
        if (isTerminal()) return Collections.emptyList();
        List<String> dependencies = new ArrayList<>();
        for (Element element : elements) {
            dependencies.addAll(element.getNonTerminalDependants());
        }
        return Collections.unmodifiableList(dependencies);
    }

    @Override
    public void removeNonTerminals() {
        // ToDo
    }
}
