package de.etgramlich.dsl.util;

import org.apache.commons.lang3.StringUtils;

import javax.lang.model.SourceVersion;
import java.util.Set;
import java.util.HashSet;
import java.util.Collection;

public final class SymbolTable {
    /**
     * Set of primitive Java types.
     */
    private static final Set<String> JAVA_TYPES = Set.of("boolean", "Boolean", "byte", "short", "int", "long", "float",
            "double", "char", "Character", "String", "Number", "Byte", "Short", "Integer", "Long", "Float", "Double");

    /**
     * Set of Scala language keywords.
     */
    private static final Set<String> SCALA_KEYWORDS = Set.of(
            "abstract", "case", "catch", "class", "def", "do", "else", "extends", "false", "final", "finally", "for",
            "forSome", "if", "implicit", "import", "lazy", "match", "new", "null", "object", "override", "package",
            "private", "protected", "return", "sealed", "super", "this", "throw", "trait", "true", "try", "type", "val",
            "var", "while", "with", "yield", ">:", "⇒", "=>", "=", "<%", "<:", "←", "<-", "#", "@", ":", "_");

    /**
     * Set of types that are no types of the language, but instead custom types.
     */
    private final Set<String> customTypes;

    /**
     * Creates symbol table with java types and no custom types.
     */
    public SymbolTable() {
        customTypes = new HashSet<>();
    }

    /**
     * Determines if the argument is either a Java type or an added custom type.
     * @param type String, must not be null.
     * @return True if type is a java or custom type.
     */
    public boolean isType(final String type) {
        return JAVA_TYPES.contains(type) || customTypes.contains(type);
    }

    /**
     * Determines whether all strings of the collection are types (language or user-defined).
     * @param types Collection, must not be null.
     * @return True if all strings match a type.
     */
    public boolean areAllTypes(final Collection<String> types) {
        return types.stream().allMatch(this::isType);
    }

    /**
     * Adds a custom type (interface or class name).
     * @param type String, must not be blank.
     */
    public void addType(final String type) {
        if (StringUtils.isBlank(type)) {
            throw new IllegalArgumentException("Type must not be blank!");
        }
        customTypes.add(type);
    }

    /**
     * Determines whether the argument is a custom type.
     * @param type String, must not be null.
     * @return True if argument was added as a type.
     */
    public boolean isCustomType(final String type) {
        return customTypes.contains(type);
    }

    /**
     * Determines whether all types are already added as custom types.
     * @param types Collection of Strings, must not be null, may be empty.
     * @return True if all elements are custom types.
     */
    public boolean allCustomType(final Collection<String> types) {
        return types.stream().allMatch(this::isCustomType);
    }

    /**
     * Determines if the string is a valid name to use by testing whether it is not a keyword and not a type.
     * @param string String.
     * @return True if it is not blank and no keyword or type.
     */
    public boolean isValidName(final String string) {
        return !StringUtil.isBlank(string) && !isKeyword(string) && !isJavaType(string);
    }

    /**
     * Returns true if the string is a java type.
     * @param string String.
     * @return True if string matches java type.
     */
    public boolean isJavaType(final String string) {
        return JAVA_TYPES.contains(string);
    }

    /**
     * Determines if the string is a java or scala keyword.
     * @param string String.
     * @return True if string is scala or java keyword.
     */
    public boolean isKeyword(final String string) {
        return isJavaKeyWord(string) || isScalaKeyword(string);
    }

    /**
     * Determines whether the passed string is a Java Keyword (by SourceVersion).
     * @param string A String, must not be null.
     * @return True if the string is a java keyword.
     */
    public boolean isJavaKeyWord(final String string) {
        if (string == null) {
            throw new IllegalArgumentException("String must not be null!");
        }
        return SourceVersion.isKeyword(string);
    }

    /**
     * Determines whether the string is a scala keyword.
     * @param string String.
     * @return True if string is scala keyword.
     */
    public boolean isScalaKeyword(final String string) {
        return SCALA_KEYWORDS.contains(string);
    }
}
