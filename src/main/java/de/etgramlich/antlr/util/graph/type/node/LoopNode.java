package de.etgramlich.antlr.util.graph.type.node;

import org.jetbrains.annotations.Contract;

import java.util.Objects;

public final class LoopNode extends Node {
    private final Node child;

    public LoopNode(final String name, final Node child) {
        super(name);
        this.child = child;
    }

    @Contract(pure = true)
    public Node getChild() {
        return child;
    }

    @Override
    public boolean isOptional() {
        return true;
    }

    @Override
    public int getTotalAmountOfChildNodes() {
        return 1 + child.getTotalAmountOfChildNodes();
    }

    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoopNode loopNode = (LoopNode) o;

        if (!getName().equals(loopNode.getName())) return false;
        return Objects.equals(child, loopNode.child);
    }

    @Override
    public int hashCode() {
        return child != null ? child.hashCode() : 0;
    }
}
