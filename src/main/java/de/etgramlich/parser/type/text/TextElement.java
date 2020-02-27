package de.etgramlich.parser.type.text;

import de.etgramlich.parser.type.BnfType;
import de.etgramlich.parser.type.Element;
import de.etgramlich.util.StringUtil;

import java.util.Collections;
import java.util.List;

/**
 * Represents a text element from BNF as Java type.
 */
public abstract class TextElement implements Element, BnfType {
    /**
     * Text from the BNF element.
     */
    private final String name;

    /**
     * Creates new TextElement from String.
     * @param name String, must not be blank!
     */
    protected TextElement(final String name) {
        if (StringUtil.isBlank(name)) {
            throw new IllegalArgumentException("Name must not be blank!");
        }
        this.name = name;
    }

    /**
     * Returns text of that BNF element.
     * @return String, not blank.
     */
    @Override
    public final String getName() {
        return name;
    }

    /**
     * Returns true if the Element does not represent another element.
     * @return True if it does not represent other element.
     */
    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public final int getNumberOfElements() {
        return 1;
    }
}
