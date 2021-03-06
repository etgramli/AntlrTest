package de.etgramlich.dsl.graph.type;

import de.etgramlich.dsl.parser.type.text.Keyword;
import de.etgramlich.dsl.parser.type.text.NonTerminal;
import de.etgramlich.dsl.parser.type.text.TextElement;

/**
 * Determines the type of a node according to the EBNF grammar.
 */
public enum NodeType {
    /**
     * Keyword (from EBNF grammar).
     */
    KEYWORD,

    /**
     * Non-Terminal (from EBNF grammar).
     */
    NON_TERMINAL,

    /**
     * Type (from EBNF grammar).
     */
    TYPE;

    /**
     * Returns the Enum Value to the according text element.
     * @param textElement Text element, must not be null!
     * @return Enum value.
     */
    public static NodeType fromTextElement(final TextElement textElement) {
        if (textElement == null) {
            throw new IllegalArgumentException("Text element must not be null!");
        }
        if (textElement instanceof Keyword) {
            return KEYWORD;
        } else if (textElement instanceof NonTerminal) {
            return NON_TERMINAL;
        } else {
            return TYPE;
        }
    }
}
