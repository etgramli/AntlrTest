package de.etgramlich.util.graph.type.node;

import java.util.*;

public final class AlternativeNode extends Node {
    private final List<Node> alternatives;

    public AlternativeNode(final String name) {
        this(name, Collections.emptyList());
    }

    public AlternativeNode(final String name, final Collection<SequenceNode> nodes) {
        super(name);
        alternatives = new ArrayList<>(nodes);
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

    public List<Node> getAlternatives() {
        return Collections.unmodifiableList(alternatives);
    }

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
