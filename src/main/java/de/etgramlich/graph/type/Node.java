package de.etgramlich.graph.type;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * A node contained in an NodeEdge to hold information from the EBNF grammar.
 */
public final class Node implements Serializable {
    /**
     * Identifier of the node.
     */
    private final String name;

    /**
     * Type of the node according to the EBNF grammar.
     */
    private final NodeType type;

    /**
     * Creates a new node with the given identifier.
     * @param name String, must not be blank.
     * @param type Type of the node.
     */
    public Node(final String name, final NodeType type) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Node name must not be blank!");
        }
        this.name = name;
        this.type = type;
    }

    /**
     * Returns the name.
     * @return String, not null, not empty.
     */
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Node node = (Node) o;

        if (!name.equals(node.name)) {
            return false;
        }
        return type == node.type;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }
}
