package de.etgramlich.antlr.util.graph;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class AlternativeNode extends Node {
    private final List<Node> children;

    public AlternativeNode(final String name, final Node successor, final Node preceding) {
        this(name, successor, preceding, Collections.emptyList());
    }

    public AlternativeNode(final String name, final Node successor, final Node preceding, final Collection<SequenceNode> nodes) {
        super(name, successor, preceding);
        children = new ArrayList<>(nodes);
    }

    public void addChild(final SequenceNode node) {
        children.add(node);
    }

    @NotNull
    @Contract(pure = true)
    public List<Node> getChildren() {
        return Collections.unmodifiableList(children);
    }
}
