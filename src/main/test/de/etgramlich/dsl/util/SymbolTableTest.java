package de.etgramlich.dsl.util;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SymbolTableTest {

    @Test
    void isType_withJavaType_returnsTrue() {
        final SymbolTable symbolTable = new SymbolTable();
        assertTrue(symbolTable.isType("byte"));
        assertTrue(symbolTable.isType("short"));
        assertTrue(symbolTable.isType("int"));
        assertTrue(symbolTable.isType("long"));
        assertTrue(symbolTable.isType("float"));
        assertTrue(symbolTable.isType("double"));
        assertTrue(symbolTable.isType("char"));
        assertTrue(symbolTable.isType("String"));
        assertTrue(symbolTable.isType("boolean"));
    }

    @Test
    void areAllTypes_withAllJavaTypes_returnsTrue() {
        final SymbolTable symbolTable = new SymbolTable();

        assertTrue(symbolTable.areAllTypes(
                Set.of("byte", "short", "int", "long", "float", "double", "char", "String", "boolean")));
    }

    @Test
    void addType_addCustomType_isTypeReturnsTrue() {
        final SymbolTable symbolTable = new SymbolTable();

        assertFalse(symbolTable.isType("MyType"));
        assertFalse(symbolTable.isCustomType("MyType"));
        symbolTable.addType("MyType");
        assertTrue(symbolTable.isType("MyType"));
        assertTrue(symbolTable.isCustomType("MyType"));
    }

    @Test
    void addType_withBlankString_throwsException() {
        final SymbolTable symbolTable = new SymbolTable();

        assertThrows(IllegalArgumentException.class, () -> symbolTable.addType("  \t  "));
    }

    @Test
    void isCustomType_withJavaType_returnsFalse() {
        final SymbolTable symbolTable = new SymbolTable();

        assertFalse(symbolTable.isCustomType("byte"));
    }

    @Test
    void allCustomType_withOnlyJavaTypes_returnsFalse() {
        final SymbolTable symbolTable = new SymbolTable();

        assertFalse(symbolTable.allCustomType(Set.of("byte", "String")));
    }

    @Test
    void allCustomTypes_withJavaAndCustomTypes_returnsFalse() {
        final SymbolTable symbolTable = new SymbolTable();
        symbolTable.addType("TypeOne");
        symbolTable.addType("TypeTwo");

        assertFalse(symbolTable.allCustomType(Set.of("TypeOne", "TypeTwo", "byte")));
    }

    @Test
    void allCustomTypes_withOnlyCustomTypes_returnsTrue() {
        final SymbolTable symbolTable = new SymbolTable();
        symbolTable.addType("TypeOne");
        symbolTable.addType("TypeTwo");

        assertTrue(symbolTable.allCustomType(Set.of("TypeOne", "TypeTwo")));
    }
}
