package de.etgramlich.parser.type;

import java.util.Collections;
import java.util.List;

/**
 * Represents a list of sequence as alternative from BNF as Java type.
 */
public final class Alternatives implements BnfType {
    /**
     * List of sequence contained in this alternatives.
     */
    private final List<Sequence> sequences;

    /**
     * Creates a new Alternatives with a copy of the provided sequence list.
     * @param sequences List of Sequence, must not be null and not empty.
     */
    public Alternatives(final List<Sequence> sequences) {
        if (sequences == null || sequences.isEmpty()) {
            throw new IllegalArgumentException("Sequences must be not null and not empty!");
        }
        this.sequences = List.copyOf(sequences);
    }

    /**
     * Returns the unmodifiable list of sequence.
     * @return List of Sequence, not null, not empty.
     */
    public List<Sequence> getSequences() {
        return sequences;
    }

    /**
     * Returns the elements in the first sequence object, otherwise an empty list.
     * @return A List of Elements, may be empty, never null.
     */
    public List<Element> getElements() {
        return sequences.size() == 1 ? sequences.get(0).getElements() : Collections.emptyList();
    }

    /**
     * Returns true if this is a true alternative with more than one Sequence element.
     * @return True if this has more than one sequence.
     */
    public boolean isAlternative() {
        return sequences.size() > 1;
    }

    /**
     * Returns true if this is not a true alternative and the first sequence has more than one element.
     * @return True if this is not an alternative, but a sequence.
     */
    public boolean isSequence() {
        return !isAlternative() && sequences.get(0).getElements().size() > 1;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public int getNumberOfElements() {
        return sequences.stream().mapToInt(Sequence::getNumberOfElements).sum();
    }

    @Override
    public List<String> getNonTerminalDependants() {
        return null;
    }
}
