package de.etgramlich.antlr.util;

import de.etgramlich.antlr.parser.listener.bnf.type.*;
import de.etgramlich.antlr.parser.listener.bnf.type.rhstype.Alternative;
import de.etgramlich.antlr.parser.listener.bnf.type.rhstype.Element;
import de.etgramlich.antlr.parser.listener.bnf.type.rhstype.repetition.AbstractRepetition;
import de.etgramlich.antlr.parser.listener.bnf.type.terminal.ID;

import java.util.List;

public interface BnfTypeVisitor {
    void visit(ID id);
    void visit(AbstractRepetition repetition);
    void visit(Alternative alternative);
    void visit(List<Alternative> alternatives);
    void visit(Element element);
    void visit(Rule rule);
    void visit(RuleList ruleList);
}
