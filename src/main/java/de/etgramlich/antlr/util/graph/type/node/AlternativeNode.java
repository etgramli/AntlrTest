package de.etgramlich.antlr.util.graph.type.node;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class AlternativeNode extends Node {
    private final List<Node> alternatives;

    public AlternativeNode(final String name) {
        this(name, Collections.emptyList());
    }

    public AlternativeNode(final String name, final Collection<SequenceNode> nodes) {
        super(name);
        alternatives = List.copyOf(nodes);
    }

    @Override
    public boolean isOptional() {
        return false;
    }

    @Override
    public int getTotalAmountOfChildNodes() {
        int counter = alternatives.size();
        for (Node node : alternatives) {
            counter += node.getTotalAmountOfChildNodes();
        }
        return counter;
    }

    public void addAlternative(final SequenceNode node) {
        alternatives.add(node);
    }

    @NotNull
    @Contract(pure = true)
    public List<Node> getAlternatives() {
        return Collections.unmodifiableList(alternatives);
    }

    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AlternativeNode that = (AlternativeNode) o;

        if (!getName().equals(that.getName())) return false;
        return alternatives.equals(that.alternatives);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + alternatives.hashCode();
        return result;
    }
}
