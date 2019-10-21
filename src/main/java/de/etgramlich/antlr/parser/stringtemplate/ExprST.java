package de.etgramlich.antlr.parser.stringtemplate;

import org.jetbrains.annotations.NotNull;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

public final class ExprST {
    private static final String ST_FILENAME = "templates.stg";
    private static final STGroupFile GROUP_FILE = new STGroupFile(ST_FILENAME);

    public static String getExprString(final String left, @NotNull final String operator, final String right) {
        if (!(operator.equals("+") | operator.equals("-"))) {
            throw new IllegalArgumentException("Operator must be + or -, but was: " + operator);
        }
        ST st = GROUP_FILE.getInstanceOf("expr");
        // ToDo: Evaluate correctness of left and right expression
        st.add("left", left);
        st.add("right", right);
        st.add("operator_add_sub", operator);
        return st.render();
    }
    public static String getTermString(final String left, @NotNull final String operator, final String right) {
        if (!(operator.equals("*") || operator.equals("/"))) {
            throw new IllegalArgumentException("Operator must be * or /, but was: " + operator);
        }
        ST st = GROUP_FILE.getInstanceOf("term");
        // ToDo: Evaluate correctness of left and right term
        st.add("left", left);
        st.add("right", right);
        st.add("operator_mul_div", operator);
        return st.render();
    }
}
