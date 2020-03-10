package de.etgramlich.dsl.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.Collection;

public final class SymbolTable {
    /**
     * Set of primitive Java types.
     */
    private static final Set<String> JAVA_TYPES = Set.of(
            "byte",
            "short",
            "int",
            "long",
            "float",
            "double",
            "char",
            "String",
            "boolean"
    );

    /**
     * Keywords usable in the grammar, that will be mapped to Java (or Scala) keywords.
     */
    public static final Set<String> KEYWORDS = Set.of(
            "component",
            "singleton",
            "impl",
            "method",
            "field"
    );

    /**
     * Mapping from grammar keywords to java keywords.
     */
    private static final Map<String, String> KEYWORD_MAPPING_JAVA = Map.of(
            "component", "class",
            "singleton", "class",
            "impl", "implements",
            "method", "",
            "field", ""
    );

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
     * @return True if type was not yet present.
     */
    public boolean addType(final String type) {
        if (StringUtils.isBlank(type)) {
            throw new IllegalArgumentException("Type must not be blank!");
        }
        return customTypes.add(type);
    }

    /**
     * Returns the java equivalent of the passed argument.
     * @param keyword String, must not be null.
     * @return String, not null.
     */
    public String getJavaMapping(final String keyword) {
        return KEYWORD_MAPPING_JAVA.get(keyword);
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
}
