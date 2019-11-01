package de.etgramlich.antlr.parser.listener.bnf;

import de.etgramlich.antlr.parser.gen.bnf.bnfBaseListener;
import de.etgramlich.antlr.parser.gen.bnf.bnfParser;
import de.etgramlich.antlr.parser.listener.bnf.type.rhstype.Alternative;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AlternativesListener extends bnfBaseListener {

    private List<Alternative> alternatives;

    @Override
    public void enterAlternatives(@NotNull bnfParser.AlternativesContext ctx) {
        List<Alternative> alternatives = new ArrayList<>();
        AlternativeListener listener = new AlternativeListener();

        for (bnfParser.AlternativeContext context : ctx.alternative()) {
            listener.enterAlternative(context);
            alternatives.add(listener.getAlternative());
        }
        this.alternatives = alternatives;
    }

    @Override
    public void exitAlternatives(bnfParser.AlternativesContext ctx) {
        super.exitAlternatives(ctx);
    }

    public List<Alternative> getAlternatives() {
        return Collections.unmodifiableList(alternatives);
    }
}
