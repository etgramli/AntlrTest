package de.etgramlich.antlr.parser.listener.number;

import de.etgramlich.antlr.parser.gen.number.NumberBaseListener;
import de.etgramlich.antlr.parser.gen.number.NumberParser;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public final class ExprToXML extends NumberBaseListener {
    private final ParseTreeProperty<String> xml = new ParseTreeProperty<>();

    private final String outFileName;

    public ExprToXML(final String outFileName) {
        this.outFileName = outFileName;
    }

    private String getXML(ParseTree node) {
        return xml.get(node);
    }

    private void setXml(@NotNull ParseTree node, String s) {
        System.out.println("Setting node " + node.toString() + " to: " + s);
        xml.put(node, s);
    }

    @Contract(pure = true)
    public ParseTreeProperty<String> getXml() {
        return xml;
    }

    private static String xmlElement(final String name, final String value) {
        return String.format("<%s>%s</%s>\n", name, value, name);
    }


    @Override
    public void exitFactor(@NotNull NumberParser.FactorContext ctx) {
        printRed(new Object(){}.getClass().getEnclosingMethod().getName());
        print(ctx.getText(), Color.GREEN);

        setXml(ctx, xmlElement("FACTOR", ctx.getText()));
    }

    @Override
    public void exitTerm(@NotNull NumberParser.TermContext ctx) {
        printRed(new Object(){}.getClass().getEnclosingMethod().getName());
        print(ctx.getText(), Color.GREEN);

        // Visit children
        ctx.factor().exitRule(this);
        if (ctx.term() != null) {
            ctx.term().exitRule(this);
            ctx.operation_term().exitRule(this);
        }

        final String xml = "<TERM>\n" +
                "<left>\n" + getXML(ctx.left) + "</left>" +
                "<operation>\n" + getXML(ctx.operation_term()) + "</operation>" +
                "<right>\n" + getXML(ctx.right) + "</right>" +
                "</TERM>";
        setXml(ctx, xml);
    }

    @Override
    public void exitOperation_term(NumberParser.Operation_termContext ctx) {
        printRed(new Object(){}.getClass().getEnclosingMethod().getName());
        print(ctx.getText(), Color.GREEN);

        setXml(ctx, xmlElement("operation_term", ctx.getText().trim()));
    }

    @Override
    public void exitExpr(@NotNull NumberParser.ExprContext ctx) {
        printRed(new Object(){}.getClass().getEnclosingMethod().getName());
        print(ctx.getText(), Color.GREEN);

        // Visit children
        ctx.term().exitRule(this);

        if (ctx.expr() != null) {
            ctx.expr().exitRule(this);
            ctx.operation_expr().exitRule(this);
        }

        final String xml = "<EXPR>\n" +
                "<left>\n" + getXML(ctx.left) + "</left>" +
                "<operation>\n" + getXML(ctx.operation_expr()) + "</operation>" +
                "<right>\n" + getXML(ctx.right) + "</right>" +
                "</EXPR>";
        setXml(ctx, xml);
    }

    @Override
    public void exitOperation_expr(@NotNull NumberParser.Operation_exprContext ctx) {
        printRed(new Object(){}.getClass().getEnclosingMethod().getName());
        print(ctx.getText(), Color.GREEN);

        setXml(ctx, xmlElement("operation_expr", ctx.getText().trim()));
    }

    @Override
    public void exitExpression(NumberParser.ExpressionContext ctx) {
        printRed(new Object(){}.getClass().getEnclosingMethod().getName());
        print(ctx.getText(), Color.GREEN);

        // Visit children
        ctx.expr().exitRule(this);

        final String xml = getXML(ctx.getChild(0));
        setXml(ctx, xml);
        try {
            Files.writeString(Paths.get(outFileName), xml, StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private enum Color {
        BLACK(30),
        RED(31),
        GREEN(32),
        WHITE(37),
        DEFAULT(39);
        private int value;
        Color(int value) {
            this.value = value;
        }
        int getValue() {
            return value;
        }
    }
    private static void print(final String string, @NotNull final Color color) {
        System.out.println((char)27 + "[" + color.getValue() + "m" + string + (char)27 + "[" + Color.DEFAULT.getValue() + "m");
    }
    private static void printRed(final String str) {
        print(str, Color.RED);
    }
}
