package de.etgramlich.antlr.util.graph;

import de.etgramlich.antlr.util.graph.node.Node;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.ParanoidGraph;

class GraphBuilderTest {
    private static final Graph<Node, ScopeEdge> graph = new ParanoidGraph<>(new DefaultDirectedGraph<>(ScopeEdge.class));
}