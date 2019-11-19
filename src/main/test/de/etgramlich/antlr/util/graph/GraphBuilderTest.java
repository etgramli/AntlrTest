package de.etgramlich.antlr.util.graph;

import de.etgramlich.antlr.util.graph.node.Node;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.ParanoidGraph;

import static org.junit.jupiter.api.Assertions.*;

class GraphBuilderTest {
    private static final Graph<Node, BnfEdge> graph = new ParanoidGraph<>(new DefaultDirectedGraph<>(BnfEdge.class));
}