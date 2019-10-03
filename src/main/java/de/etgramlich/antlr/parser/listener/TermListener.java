package de.etgramlich.antlr.parser.listener;

import de.etgramlich.antlr.parser.gen.NumberBaseListener;
import de.etgramlich.antlr.parser.gen.NumberParser;

import java.util.List;
import java.util.stream.Collectors;

public class TermListener extends NumberBaseListener {

    private int result = 0; // Default value 0, because in addition/subtraction 0 is the neutral element

    @Override
    public void enterTerm(NumberParser.TermContext ctx) {
        FactorListener fl = new FactorListener();
        ctx.factor().enterRule(fl);
        int factor = fl.getFactor();

        if (ctx.term() == null) {
            result = factor;
            return;
        }

        TermListener tl = new TermListener();
        ctx.term().enterRule(tl);
        int termResult = tl.getResult();

        OperationTermListener otl = new OperationTermListener();
        ctx.operation_term().enterRule(otl);
        result = otl.getResult(factor, termResult);
    }

    @Override
    public void exitTerm(NumberParser.TermContext ctx) {
        System.out.println("TERM: " + ctx.getText() + "\t has result: " + result);
    }

    public int getResult() {
        return result;
    }
}
