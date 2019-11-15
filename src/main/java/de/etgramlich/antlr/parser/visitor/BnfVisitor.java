package de.etgramlich.antlr.parser.visitor;

import de.etgramlich.antlr.parser.type.*;
import de.etgramlich.antlr.parser.type.rhstype.Alternative;
import de.etgramlich.antlr.parser.type.rhstype.Element;
import de.etgramlich.antlr.parser.type.rhstype.LetterRange;
import de.etgramlich.antlr.parser.type.rhstype.repetition.Optional;
import de.etgramlich.antlr.parser.type.rhstype.repetition.Precedence;
import de.etgramlich.antlr.parser.type.rhstype.repetition.ZeroOrMore;
import de.etgramlich.antlr.parser.type.terminal.AbstractId;
import de.etgramlich.antlr.util.graph.node.Node;
import de.etgramlich.antlr.util.visitor.BnfTypeVisitor;

import java.util.List;

/**
 * Class that builds a dependency graph to generate a hierarchy of scopes to be generated.
 */
public class BnfVisitor implements BnfTypeVisitor {
    private Node graph;

    public BnfVisitor(final RuleList ruleList) {
        visit(ruleList);
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
    public void visit(Optional optional) {
        // ToDo
        throw new UnsupportedOperationException("Not yet implemented!!!");
    }

    @Override
    public void visit(ZeroOrMore repetition) {
        // ToDo
        throw new UnsupportedOperationException("Not yet implemented!!!");
    }

    @Override
    public void visit(Precedence precedence) {
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
