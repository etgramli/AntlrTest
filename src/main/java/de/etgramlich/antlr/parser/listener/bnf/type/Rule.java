package de.etgramlich.antlr.parser.listener.bnf.type;

import de.etgramlich.antlr.parser.listener.bnf.type.terminal.ID;
import de.etgramlich.antlr.util.BnfElement;
import de.etgramlich.antlr.util.BnfTypeVisitor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


/**
 * ToDo: lhs -> Interface
 * ToDo: rhs -> Methods
 *
 * Rhs: respect call sequence and mutual exclusive rules
 */
public final class Rule implements BnfElement {
    private final ID lhs;
    private final Alternatives rhs;

    @Contract(pure = true)
    public Rule(final ID lhs, final Alternatives rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Contract(pure = true)
    public ID getLhs() {
        return lhs;
    }

    @Contract(pure = true)
    public Alternatives getRhs() {
        return rhs;
    }

    @Override
    public void accept(@NotNull BnfTypeVisitor visitor) {
        lhs.accept(visitor);
        rhs.accept(visitor);
        visitor.visit(this);
    }
}
