package de.etgramlich.util;

import de.etgramlich.parser.type.BnfType;

import javax.lang.model.SourceVersion;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton class to store symbols of the parsed EBNF grammar.
 */
public final class SymbolTable {
    private static final Map<String, BnfType> SYMBOL_TABLE = new ConcurrentHashMap<>();
    private static final Map<String, Boolean> RULES = new ConcurrentHashMap<>();

    private SymbolTable() { }

    public static boolean containsRule(final String ruleName) {
        return RULES.containsKey(ruleName);
    }

    public static void addRule(final String name, final boolean isTerminal) {
        RULES.put(name, isTerminal);
    }

    public static boolean isTerminal(final String rule) {
        return RULES.get(rule);
    }

    public static boolean contains(final String symbol) {
        return SYMBOL_TABLE.containsKey(symbol);
    }

    public static boolean add(final String symbol, final BnfType type) {
        if (contains(symbol))
            return false;
        else {
            SYMBOL_TABLE.put(symbol, type);
            return true;
        }
    }

    public static BnfType getType(final String symbol) {
        return SYMBOL_TABLE.get(symbol);
    }

    public static int getSize() {
        return SYMBOL_TABLE.size();
    }

    public static boolean isKeyword(final String id) {
        return SourceVersion.isKeyword(id);
    }
}
