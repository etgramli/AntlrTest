package de.etgramlich.antlr.util.visitor;

import de.etgramlich.antlr.parser.type.*;
import de.etgramlich.antlr.parser.type.rhstype.Alternative;
import de.etgramlich.antlr.parser.type.rhstype.Element;
import de.etgramlich.antlr.parser.type.rhstype.LetterRange;
import de.etgramlich.antlr.parser.type.rhstype.repetition.Optional;
import de.etgramlich.antlr.parser.type.rhstype.repetition.Precedence;
import de.etgramlich.antlr.parser.type.rhstype.repetition.ZeroOrMore;
import de.etgramlich.antlr.parser.type.terminal.AbstractId;

import java.util.List;

public interface BnfTypeVisitor {
    void visit(AbstractId id);
    void visit(LetterRange letterRange);
    void visit(Optional repetition);
    void visit(ZeroOrMore repetition);
    void visit(Precedence precedence);
    void visit(Alternative alternative);
    void visit(List<Alternative> alternatives);
    void visit(Element element);
    void visit(Rule rule);
    void visit(RuleList ruleList);
}
