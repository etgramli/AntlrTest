package de.etgramlich.antlr.parser.listener.bnf.type.terminal;

import de.etgramlich.antlr.parser.listener.bnf.IdListener;

public class Id extends RuleId {
    public Id(String id) {
        super(IdListener.stripLTGT(id));
    }
}
