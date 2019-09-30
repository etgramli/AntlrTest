package de.etgramlich.antlr.parser.listener;

import de.etgramlich.antlr.parser.gen.NumberBaseListener;
import de.etgramlich.antlr.parser.gen.NumberParser;

public class FactorListener extends NumberBaseListener {

    private int factor = 1; // Default value is 1, because in multiplication/division 1 is the neutral element

    @Override
    public void enterFactor(NumberParser.FactorContext ctx) {
        factor = Integer.parseInt(ctx.getText());
    }

    @Override
    public void exitFactor(NumberParser.FactorContext ctx) {
        System.out.println("FACTOR: " + factor);
    }


    public int getFactor() {
        return factor;
    }
}
