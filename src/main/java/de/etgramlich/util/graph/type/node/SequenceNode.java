package de.etgramlich.util.graph.type.node;

import java.util.Objects;

public final class SequenceNode extends Node {
    private final boolean isOptional;

    public SequenceNode(final String name) {
        this(name, null);
    }

    public SequenceNode(final String name, final boolean isOptional) {
        this(name, null, isOptional);
    }

    public SequenceNode(final String name, final SequenceNode successor) {
        this(name, successor, false);
    }

    public SequenceNode(final String name, final SequenceNode successor, final boolean isOptional) {
        super(name, successor);
        this.isOptional = isOptional;
    }


    @Override
    public boolean isOptional() {
        return isOptional;
    }

    @Override
    public int getTotalAmountOfChildNodes() {
        return 1 + (getSuccessor() != null ? getSuccessor().getTotalAmountOfChildNodes() : 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SequenceNode that = (SequenceNode) o;

        if (isOptional != that.isOptional) return false;
        return Objects.equals(getSuccessor(), that.getSuccessor());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getSuccessor() != null ? getSuccessor().hashCode() : 0);
        result = 31 * result + (isOptional ? 1 : 0);
        return result;
    }
}
