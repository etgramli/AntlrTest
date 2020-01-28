package de.etgramlich.parser.type;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class Alternatives implements BnfType {
    private final List<Sequence> sequences;

    public Alternatives(final Collection<Sequence> sequences) {
        if (sequences == null || sequences.isEmpty()) {
            throw new IllegalArgumentException("Sequences must be not null and not empty!");
        }
        this.sequences = List.copyOf(sequences);
    }

    public List<Sequence> getSequences() {
        return sequences;
    }

    public List<Element> getElements() {
        return sequences.size() == 1 ? sequences.get(0).getElements() : Collections.emptyList();
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
    public List<String> getNonTerminalDependants() {
        return null;
    }
}
