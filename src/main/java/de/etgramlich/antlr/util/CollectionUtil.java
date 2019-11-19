package de.etgramlich.antlr.util;

import de.etgramlich.antlr.util.graph.node.Node;
import de.etgramlich.antlr.util.graph.node.SequenceNode;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class CollectionUtil {
    @Contract(pure = true)
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
    @NotNull
    @Contract("!null -> new")
    public static <T> List<T> toList(T... items) {
        if (items == null) {
            return new ArrayList<>();
        } else {
            return new ArrayList<>(Arrays.asList(items));
        }
    }

    /**
     * Converts a sequence of SequenceNodes (basically a linked list) to an unmodifiable List.
     * @param node A SequenceNode, must not be null.
     * @return A non-empty, unmodifiable List of SequenceNodes.
     */
    @NotNull
    public static List<Node> toList(@NotNull final SequenceNode node) {
        List<SequenceNode> nodeList = new ArrayList<>();
        nodeList.add(node);

        SequenceNode currentNode = node;
        while (currentNode.getSuccessor() != null) {
            currentNode = currentNode.getSuccessor();
            nodeList.add(currentNode);
        }

        return Collections.unmodifiableList(nodeList);
    }
}
