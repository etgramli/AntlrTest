package de.etgramlich.parser.type;

import java.util.List;

public interface BnfType {
    /**
     * Returns the name of the bnf element, determined by an id, text or a containing element.
     * @return String, must not be null.
     */
    String getName();

    /**
     * Returns true if the type or rule only contains terminal elements.
     * @return True if all parts are terminal, else false.
     */
    default boolean isTerminal() {
        return false;
    }

    /**
     * Returns a list of IDs of the non-terminals this thingy depends on.
     * @return List of Strings, not null.
     */
    List<String> getNonTerminalDependants();
}