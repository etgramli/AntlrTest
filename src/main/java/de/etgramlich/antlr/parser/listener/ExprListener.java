package de.etgramlich.antlr.parser.listener;

import de.etgramlich.antlr.parser.gen.NumberBaseListener;
import de.etgramlich.antlr.parser.gen.NumberParser;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ExprListener extends NumberBaseListener {

    // Has no default value, because it is the uppermost node and not used recursively
    private Optional<Integer> result = Optional.empty();

    @Override
    public void enterExpr(NumberParser.ExprContext ctx) {
        TermListener tl = new TermListener();
        final List<Integer> termResults = ctx.term().stream().map(f -> {f.enterRule(tl); return tl.getResult();})
                .collect(Collectors.toUnmodifiableList());

        if (termResults.size() == 0) {
            System.err.println("Expression empty!!!! " + ctx);
            return;
        }
        if (termResults.size() == 1) {
            result = Optional.of(termResults.get(0));
        } else {
            OperationExprListener oel = new OperationExprListener();
            int term_left = termResults.get(0);
            for (int i = 1; i < termResults.size(); ++i) {
                int term_right = termResults.get(i);
                ctx.operation_expr(i-1).enterRule(oel);

                term_left = oel.getResult(term_left, term_right);
            }
            result = Optional.of(term_left);
        }
        System.out.println("EXPRESSION: " + ctx.getText() + "\t has result: " + result);
    }

    public Optional<Integer> getResult() {
        return result;
    }
}
