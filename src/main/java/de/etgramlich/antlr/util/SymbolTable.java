package de.etgramlich.antlr.util;

import de.etgramlich.antlr.parser.type.BnfType;
import org.jetbrains.annotations.Contract;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Singleton class to store symbols of the parsed EBNF grammar.
 */
public final class SymbolTable {
    private static final Map<String, BnfType> symbolTable = new HashMap<>();
    private static final Map<String, Boolean> rules = new HashMap<>();

    @Contract(pure = true)
    private SymbolTable() {}

    @Contract(pure = true)
    public static boolean containsRule(final String ruleName) {
        return rules.containsKey(ruleName);
    }
    public static void addRule(final String name, final boolean isTerminal) {
        rules.put(name, isTerminal);
    }
    public static boolean isTerminal(final String rule) {
        return rules.get(rule);
    }

    @Contract(pure = true)
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

    public enum SymbolType {
        SCOPE,
        ID
    }
}
