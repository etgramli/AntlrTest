package de.etgramlich.graph.type;

import de.etgramlich.parser.type.text.Keyword;
import de.etgramlich.parser.type.text.NonTerminal;
import de.etgramlich.parser.type.text.TextElement;

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

    public static NodeType fromTextElement(final TextElement textElement) {
        if (textElement instanceof Keyword) {
            return KEYWORD;
        } else if (textElement instanceof NonTerminal) {
            return NON_TERMINAL;
        } else {
            return TYPE;
        }
    }
}
