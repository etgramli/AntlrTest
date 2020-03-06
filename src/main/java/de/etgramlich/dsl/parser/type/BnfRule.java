package de.etgramlich.dsl.parser.type;

import de.etgramlich.dsl.parser.type.text.NonTerminal;

/**
 * Represents a BNF rule as Java type.
 */
public final class BnfRule implements BnfType {
    /**
     * Left-hand side of the rule, it is an identifier / non-terminal.
     */
    private final NonTerminal lhs;

    /**
     * Right-hand side of the rule, an Alternative BNF element.
     */
    private final Alternatives rhs;

    /**
     * Creates a new BNF rule element from rhs and lhs.
     * @param lhs Non terminal element, must not be null.
     * @param rhs Alternatives element, must not be null.
     */
    public BnfRule(final NonTerminal lhs, final Alternatives rhs) {
        if (lhs == null) {
            throw new IllegalArgumentException("Lhs must not be null!");
        }
        if (rhs == null) {
            throw new IllegalArgumentException("Rhs must not be null!");
        }
        this.lhs = lhs;
        this.rhs = rhs;
    }

    /**
     * Returns the left-hand-side.
     * @return LHS, not null.
     */
    public NonTerminal getLhs() {
        return lhs;
    }

    /**
     * Returns the rhs Alternatives element.
     * @return Alternatives, not null.
     */
    public Alternatives getRhs() {
        return rhs;
    }

    /**
     * Determines if the element is a real alternative (or if it has only one element).
     * @return True if the RHS has only one sequence element.
     */
    public boolean hasOnlyOneAlternative() {
        return rhs.getSequences().size() == 1;
    }

    @Override
    public String getName() {
        return lhs.getName();
    }

    @Override
    public boolean isTerminal() {
        return rhs.getSequences().stream().filter(alternative -> !alternative.isTerminal()).findAny().isEmpty();
    }

    /**
     * Determine if this rule is a start rule. This is the case when the rhs has only one child element and this is a
     * non terminal text element.
     * @return True if rhs has only one non terminal element.
     */
    public boolean isStartRule() {
        return !isTerminal() && hasOnlyOneAlternative()
                && rhs.getElements().size() == 1 && rhs.getElements().get(0) instanceof NonTerminal;
    }

    @Override
    public int getNumberOfElements() {
        return rhs.getNumberOfElements();
    }
}
