package de.etgramlich.antlr.util;

import de.etgramlich.antlr.parser.listener.bnf.type.*;
import de.etgramlich.antlr.parser.listener.bnf.type.repetition.AbstractRepetition;
import de.etgramlich.antlr.parser.listener.bnf.type.terminal.ID;

public interface BnfTypeVisitor {
    void visit(ID id);
    void visit(AbstractRepetition repetition);
    void visit(Alternative alternative);
    void visit(Alternatives alternatives);
    void visit(Element element);
    void visit(Rule rule);
    void visit(RuleList ruleList);
}
