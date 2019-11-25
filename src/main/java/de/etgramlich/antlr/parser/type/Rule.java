package de.etgramlich.antlr.parser.type;

import de.etgramlich.antlr.parser.type.rhstype.Alternative;
import de.etgramlich.antlr.parser.type.terminal.AbstractId;
import de.etgramlich.antlr.util.SymbolTable;
import de.etgramlich.antlr.util.visitor.BnfElement;
import de.etgramlich.antlr.util.visitor.BnfTypeVisitor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;


/**
 * Rhs: respect call sequence and mutual exclusive rules
 */
public final class Rule implements BnfType, BnfElement {
    private static final String INTERFACE_ST_FILENAME = "src/main/resources/ebnf.stg";

    private final AbstractId lhs;
    private final List<Alternative> rhs;

    @Contract(pure = true)
    public Rule(final AbstractId lhs, final List<Alternative> rhs) {
        this.lhs = lhs;
        this.rhs = List.copyOf(rhs);
    }

    @Contract(pure = true)
    public AbstractId getLhs() {
        return lhs;
    }

    @Contract(pure = true)
    public List<Alternative> getRhs() {
        return rhs;
    }


    @Contract(pure = true)
    public boolean hasOnlyOneAlternative() {
        return rhs.size() == 1;
    }

    @Override
    public String getName() {
        return lhs.getName();
    }

    @Override
    public boolean isTerminal() {
        return rhs.stream().filter(alternative -> !alternative.isTerminal()).findAny().isEmpty();
    }

    @Override
    public List<String> getNonTerminalDependants() {
        if (isTerminal()) return Collections.emptyList();
        List<String> nonTerminals = new ArrayList<>();
        for (Alternative alternative : rhs) {
            nonTerminals.addAll(alternative.getNonTerminalDependants());
        }
        return Collections.unmodifiableList(nonTerminals);
    }

    @Override
    public void removeNonTerminals() {
        // ToDo: Replace rhs Alternatives in-place
        for (int i = 0; i < rhs.size(); ++i) {
            if (rhs.get(i).isTerminal()) continue;
            BnfType terminal = SymbolTable.getType(rhs.get(i).getName());
        }
    }

    @Override
    public void accept(@NotNull BnfTypeVisitor visitor) {
        visitor.visit(this);
        rhs.forEach(alternative -> alternative.accept(visitor));
    }

    private static class Method {
        private final String returnType, name;
        private Method(final String returnType, final String name) {
            this.returnType = returnType;
            this.name = name;
        }
        public String getName() {
            return name;
        }
        public String getReturnType() {
            return returnType;
        }
    }
}
