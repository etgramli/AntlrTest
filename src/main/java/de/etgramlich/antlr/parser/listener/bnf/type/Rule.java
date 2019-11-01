package de.etgramlich.antlr.parser.listener.bnf.type;

import de.etgramlich.antlr.parser.listener.bnf.type.rhstype.Alternative;
import de.etgramlich.antlr.parser.listener.bnf.type.rhstype.Element;
import de.etgramlich.antlr.parser.listener.bnf.type.terminal.ID;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * ToDo: lhs -> Interface
 * ToDo: rhs -> Methods (return types may be interfaces or simple types)
 *
 * ToDo: Keep track of all types and make them return types in RHS
 *
 * Rhs: respect call sequence and mutual exclusive rules
 */
public final class Rule implements BnfType {
    private final ID lhs;
    private final List<Alternative> rhs;

    @Contract(pure = true)
    public Rule(final ID lhs, final List<Alternative> rhs) {
        this.lhs = lhs;
        this.rhs = List.copyOf(rhs);
    }

    @Contract(pure = true)
    public ID getLhs() {
        return lhs;
    }

    @Contract(pure = true)
    public List<Alternative> getRhs() {
        return rhs;
    }


    private static final String NEWLINE = "\n";
    private static final String TABULATOR = "\t";
    private static final String METHOD_END = "();";
    private static final String INT_TYPE = "int ";

    @NotNull
    public String buildInterface() {
        StringBuilder sb = new StringBuilder("interface ");

        sb.append(lhs.getText()).append(" {").append(NEWLINE);

        Set<String> encounteredNames = new HashSet<>();
        for (Alternative alternative : rhs) {
            for (Element element : alternative.getElements()) {
                // ToDo: test for alternatives in element (is recursive)
                if (element.getId() != null && encounteredNames.add(element.getId().getText())) {
                    sb.append(TABULATOR)
                            .append(INT_TYPE)
                            .append(element.getId().getText())
                            .append(METHOD_END)
                            .append(NEWLINE);
                }
            }
        }

        return sb.append("}\n").toString();
    }
}
