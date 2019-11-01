package de.etgramlich.antlr.util;

import org.jetbrains.annotations.Contract;

import java.util.HashMap;
import java.util.Map;

public final class SymbolTable {
    // Maybe change to nested Map to detect duplicates within interface
    private static final Map<String, SymbolType> symbolTable = new HashMap<>();

    @Contract(pure = true)
    private SymbolTable() {}

    @Contract(pure = true)
    public static boolean contains(final String symbol) {
        return symbolTable.containsKey(symbol);
    }

    public static boolean add(final String symbol, final SymbolType type) {
        if (contains(symbol))
            return false;
        else {
            symbolTable.put(symbol, type);
            return true;
        }
    }

    public static SymbolType getType(final String symbol) {
        return symbolTable.get(symbol);
    }

    public static int getSize() {
        return symbolTable.size();
    }

    public enum SymbolType {
        LHS,
        RHS
    }
}
