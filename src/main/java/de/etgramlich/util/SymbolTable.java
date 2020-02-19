package de.etgramlich.util;

import de.etgramlich.parser.type.BnfType;

import javax.lang.model.SourceVersion;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton class to store symbols of the parsed EBNF grammar.
 */
public final class SymbolTable {
    private static final Map<String, BnfType> symbolTable = new ConcurrentHashMap<>();
    private static final Map<String, Boolean> rules = new ConcurrentHashMap<>();

    private SymbolTable() {
    }

    public static boolean containsRule(final String ruleName) {
        return rules.containsKey(ruleName);
    }

    public static void addRule(final String name, final boolean isTerminal) {
        rules.put(name, isTerminal);
    }

    public static boolean isTerminal(final String rule) {
        return rules.get(rule);
    }

    public static boolean contains(final String symbol) {
        return symbolTable.containsKey(symbol);
    }

    public static boolean add(final String symbol, final BnfType type) {
        if (contains(symbol))
            return false;
        else {
            symbolTable.put(symbol, type);
            return true;
        }
    }

    public static BnfType getType(final String symbol) {
        return symbolTable.get(symbol);
    }

    public static int getSize() {
        return symbolTable.size();
    }

    public static boolean isKeyword(final String id) {
        return SourceVersion.isKeyword(id);
    }
}
