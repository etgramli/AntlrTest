package de.etgramlich.antlr.parser.visitor.bnf;

import de.etgramlich.antlr.parser.listener.bnf.type.*;
import de.etgramlich.antlr.parser.listener.bnf.type.rhstype.Alternative;
import de.etgramlich.antlr.parser.listener.bnf.type.rhstype.Element;
import de.etgramlich.antlr.parser.listener.bnf.type.rhstype.repetition.AbstractRepetition;
import de.etgramlich.antlr.parser.listener.bnf.type.terminal.ID;
import de.etgramlich.antlr.util.BnfTypeVisitor;

import java.util.List;

public class BnfVisitor implements BnfTypeVisitor {
    @Override
    public void visit(ID id) {
        // ToDo
        throw new UnsupportedOperationException("Not yet implemented!!!");
    }

    @Override
    public void visit(AbstractRepetition repetition) {
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
