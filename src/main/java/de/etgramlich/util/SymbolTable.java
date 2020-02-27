package de.etgramlich.util;

import de.etgramlich.parser.type.BnfType;

import javax.lang.model.SourceVersion;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton class to store symbols of the parsed EBNF grammar.
 */
public final class SymbolTable {
    /**
     * All Java language keywords.
     */
    private static final List<String> JAVA_KEYWORDS = List.of("abstract", "assert", "boolean", "break", "byte", "case",
            "catch", "char", "class", "const", "continue", "default", "do", "double", "else", "enum", "extends",
            "final", "finally", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long",
            "new", "package", "private", "protected", "public", "return", "short", "static", "strictfp", "switch",
            "synchronized", "this", "throw", "throws", "transient", "try", "void", "volatile");
    /**
     * Scala language keywords.
     */
    private static final List<String> SCALA_KEYWORDS = List.of("abstract", "case", "catch", "class", "def", "do",
            "else", "extends", "false", "final", "finally", "for", "forSome", "if", "implicit", "import", "lazy",
            "match", "new", "null", "object", "override", "package", "private", "protected", "return", "sealed",
            "super", "this", "throw", "trait", "true", "try", "type", "val", "var", "while", "with", "yield");

    static {
        for (String keyword: JAVA_KEYWORDS) {
            assert (isKeyword(keyword));
        }
    }


    /**
     * Stores name of rules and rule.
     */
    private static final Map<String, BnfType> SYMBOL_TABLE = new ConcurrentHashMap<>();

    /**
     * Stores types and if they are terminal or not.
     */
    private static final Map<String, Boolean> RULES = new ConcurrentHashMap<>();

    private SymbolTable() { }

    /**
     * Adds a name to the symbol table and whether it is terminal.
     * @param name Identifier.
     * @param isTerminal If the identifier is terminal.
     */
    public static void add(final String name, final boolean isTerminal) {
        RULES.put(name, isTerminal);
    }

    /**
     * Tests if rule is terminal.
     * @param rule Rule name.
     * @return True if it is termnal.
     */
    public static boolean isTerminal(final String rule) {
        return RULES.get(rule);
    }

    /**
     * Tests if the symbol is in the symbol table.
     * @param symbol String, must not be null.
     * @return True if symbol exists in symbol table.
     */
    public static boolean contains(final String symbol) {
        return RULES.containsKey(symbol);
    }

    /**
     * Number of entries in symbol table.
     * @return Non-negative integer.
     */
    public static int getSize() {
        return RULES.size();
    }

    /**
     * Determines if id is a java keyword.
     * @param id String, must not be blank.
     * @return True if id is a keyword.
     */
    public static boolean isKeyword(final String id) {
        return SourceVersion.isKeyword(id);
    }

    /**
     * Add bnf element to symbol table.
     * @param symbol Name of the rule.
     * @param type Rule type to that name.
     * @return False if rule already exists.
     */
    public static boolean add(final String symbol, final BnfType type) {
        if (contains(symbol)) {
            return false;
        } else {
            SYMBOL_TABLE.put(symbol, type);
            return true;
        }
    }

    /**
     * Returns bnf type to that symbol.
     * @param symbol String, must not be blank.
     * @return Bnf type.
     */
    public static BnfType getType(final String symbol) {
        return SYMBOL_TABLE.get(symbol);
    }
}
