package de.etgramlich.util.graph.type.node;

public final class SequenceNode extends Node {
    private final boolean isOptional;

    public SequenceNode(final String name) {
        this(name, false);
    }

    public SequenceNode(final String name, final boolean isOptional) {
        super(name);
        this.isOptional = isOptional;
    }


    @Override
    public boolean isOptional() {
        return isOptional;
    }

    @Override
    public int getTotalAmountOfChildNodes() {
        return 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SequenceNode that = (SequenceNode) o;

        return isOptional == that.isOptional;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (isOptional ? 1 : 0);
        return result;
    }
}
