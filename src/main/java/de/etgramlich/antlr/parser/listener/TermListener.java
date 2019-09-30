package de.etgramlich.antlr.parser.listener;

import de.etgramlich.antlr.parser.gen.NumberBaseListener;
import de.etgramlich.antlr.parser.gen.NumberParser;

import java.util.List;
import java.util.stream.Collectors;

public class TermListener extends NumberBaseListener {

    private int result = 0; // Default value 0, because in addition/subtraction 0 is the neutral element

    @Override
    public void enterTerm(NumberParser.TermContext ctx) {
        assert (ctx.factor().size() == ctx.operation_term().size() + 1);

        FactorListener fl = new FactorListener();
        final List<Integer> factors = ctx.factor().stream().map(f -> {
            f.enterRule(fl);
            return fl.getFactor();
        }).collect(Collectors.toUnmodifiableList());

        result = factors.get(0);
        if (factors.size() > 1) {
            OperationTermListener otl = new OperationTermListener();
            for (int i = 1; i < factors.size(); ++i) {
                ctx.operation_term(i - 1).enterRule(otl);
                result = otl.getResult(result, factors.get(i));
            }
        }
    }

    @Override
    public void exitTerm(NumberParser.TermContext ctx) {
        System.out.println("TERM: " + ctx.getText() + "\t has result: " + result);
    }

    public int getResult() {
        return result;
    }
}
