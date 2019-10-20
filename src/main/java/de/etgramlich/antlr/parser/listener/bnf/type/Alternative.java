package de.etgramlich.antlr.parser.listener.bnf.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Alternative {
    private List<Element> elements;

    public Alternative(final Collection<Element> elements) {
        this.elements = new ArrayList<>(elements);
    }

    public List<Element> getElements() {
        return Collections.unmodifiableList(elements);
    }
}
