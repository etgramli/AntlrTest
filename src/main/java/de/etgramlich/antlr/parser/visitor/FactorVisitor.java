package de.etgramlich.antlr.parser.visitor;

import de.etgramlich.antlr.parser.gen.NumberBaseVisitor;
import de.etgramlich.antlr.parser.gen.NumberParser;

public class FactorVisitor extends NumberBaseVisitor<Integer> {

    @Override
    public Integer visitFactor(NumberParser.FactorContext ctx) {
        return Integer.valueOf(ctx.getText());
    }
}
