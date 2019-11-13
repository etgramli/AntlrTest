package de.etgramlich.antlr.parser.visitor;

import de.etgramlich.antlr.parser.listener.type.*;
import de.etgramlich.antlr.parser.listener.type.rhstype.Alternative;
import de.etgramlich.antlr.parser.listener.type.rhstype.Element;
import de.etgramlich.antlr.parser.listener.type.rhstype.LetterRange;
import de.etgramlich.antlr.parser.listener.type.rhstype.repetition.OneOrMore;
import de.etgramlich.antlr.parser.listener.type.rhstype.repetition.Optional;
import de.etgramlich.antlr.parser.listener.type.rhstype.repetition.ZeroOrMore;
import de.etgramlich.antlr.parser.listener.type.terminal.AbstractId;
import de.etgramlich.antlr.util.visitor.BnfTypeVisitor;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.List;

/**
 * Class that builds a dependency graph to generate a hierarchy of scopes to be generated.
 */
public class BnfVisitor implements BnfTypeVisitor {
    private final Graph<BnfType, DefaultEdge> graph;

    private String currentScopeName;
    private List<String> precedingScopeNames;
    private List<String> followingScopeNames;
    private List<String> alternativesNames;

    public BnfVisitor() {
        graph = new DefaultDirectedGraph<>(DefaultEdge.class);
    }

    @Override
    public void visit(AbstractId id) {
        // ToDo
        throw new UnsupportedOperationException("Not yet implemented!!!");
    }

    @Override
    public void visit(LetterRange letterRange) {
        // ToDo
        throw new UnsupportedOperationException("Not yet implemented!!!");
    }

    @Override
    public void visit(OneOrMore repetition) {
        // ToDo
        throw new UnsupportedOperationException("Not yet implemented!!!");
    }

    @Override
    public void visit(Optional repetition) {

    }

    @Override
    public void visit(ZeroOrMore repetition) {
        // ToDo
        throw new UnsupportedOperationException("Not yet implemented!!!");
    }

    @Override
    public void visit(Alternative alternative) {
        // ToDo
        throw new UnsupportedOperationException("Not yet implemented!!!");
    }

    @Override
    public void visit(List<Alternative> alternatives) {
        // ToDo
        throw new UnsupportedOperationException("Not yet implemented!!!");
    }

    @Override
    public void visit(Element element) {
        // ToDo
        throw new UnsupportedOperationException("Not yet implemented!!!");
    }

    @Override
    public void visit(Rule rule) {
        // ToDo
        throw new UnsupportedOperationException("Not yet implemented!!!");
    }

    @Override
    public void visit(RuleList ruleList) {
        // ToDo
        throw new UnsupportedOperationException("Not yet implemented!!!");
    }
}
