package de.etgramlich.parser.type;

import de.etgramlich.util.StringUtil;

import java.util.Collections;
import java.util.List;

public abstract class TextElement implements Element, BnfType {
    private final String name;

    public TextElement(final String name) {
        if (StringUtil.isBlank(name)) {
            throw new IllegalArgumentException("Name must not be blank!");
        }
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public int getNumberOfElements() {
        return 1;
    }

    @Override
    public List<String> getNonTerminalDependants() {
        return Collections.emptyList();
    }
}
