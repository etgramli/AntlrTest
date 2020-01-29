package de.etgramlich.util;

import de.etgramlich.util.graph.type.node.Node;
import de.etgramlich.util.graph.type.node.SequenceNode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class CollectionUtil {
    private CollectionUtil() {}

    /**
     * Creates a list of the objects passed as varargs. Never returns null,
     * instead returns an empty list when null is passed as array, or a List containing
     * a null object when null is passes as Object.
     *
     * @param items (Array) containing the elements to convert to list.
     * @param <T> The type of objects passed.
     * @return A list, that might be empty or contain null objects.
     */
    @SafeVarargs
    public static <T> List<T> toList(T... items) {
        final List<T> itemsList = items != null ? Arrays.asList(items) : Collections.emptyList();
        return new ArrayList<>(itemsList);
    }

    /**
     * Converts a sequence of SequenceNodes (basically a linked list) to an unmodifiable List.
     * @param node A SequenceNode, must not be null.
     * @return A non-empty, unmodifiable List of SequenceNodes.
     */
    public static List<Node> toList(@NotNull final SequenceNode node) {
        List<Node> nodeList = new ArrayList<>();
        nodeList.add(node);

        Node currentNode = node;
        while (currentNode.getSuccessor() != null) {
            currentNode = currentNode.getSuccessor();
            nodeList.add(currentNode);
        }

        return Collections.unmodifiableList(nodeList);
    }
}
