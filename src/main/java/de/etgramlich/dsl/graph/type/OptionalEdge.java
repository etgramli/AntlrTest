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
        int hashCode = getSource() != null ? getSource().hashCode() : 0;
        hashCode = 31 * hashCode + (getTarget() != null ? getTarget().hashCode() : 0);
        return hashCode;
    }
}
