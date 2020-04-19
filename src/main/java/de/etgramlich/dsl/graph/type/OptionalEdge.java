package de.etgramlich.dsl.graph.type;

/**
 * An empty edge indicating that a parallel edge in the same direction can be omitted.
 */
public final class OptionalEdge extends ScopeEdge {
    private static final long serialVersionUID = -6418301968619324713L;

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OptionalEdge optionalEdge = (OptionalEdge) o;

        if (getSource() != optionalEdge.getSource()) {
            return false;
        }
        return getTarget() == optionalEdge.getTarget();
    }

    @Override
    public int hashCode() {
        return 31 * getSource().hashCode() + getTarget().hashCode();
    }
}
