package de.etgramlich.dsl.parser.type.text;

import de.etgramlich.dsl.parser.type.BnfType;
import de.etgramlich.dsl.parser.type.Element;
import de.etgramlich.dsl.util.StringUtil;

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
     * @return True if it does not represent the other element.
     */
    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public final int getNumberOfElements() {
        return 1;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TextElement that = (TextElement) o;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public abstract String toString();
}
