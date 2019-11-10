package de.etgramlich.antlr.util;

import de.etgramlich.antlr.parser.listener.type.*;
import de.etgramlich.antlr.parser.listener.type.rhstype.Alternative;
import de.etgramlich.antlr.parser.listener.type.rhstype.Element;
import de.etgramlich.antlr.parser.listener.type.rhstype.repetition.AbstractRepetition;
import de.etgramlich.antlr.parser.listener.type.terminal.AbstractId;

import java.util.List;

public interface BnfTypeVisitor {
    void visit(AbstractId id);
    void visit(AbstractRepetition repetition);
    void visit(Alternative alternative);
    void visit(List<Alternative> alternatives);
    void visit(Element element);
    void visit(Rule rule);
    void visit(RuleList ruleList);
}
