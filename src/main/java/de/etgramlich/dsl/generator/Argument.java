package de.etgramlich.dsl.generator;

import org.apache.commons.lang3.StringUtils;

/**
 * Represents a argument of a method. Used in an intermediate step before writing interfaces to files.
 */
public final class Argument {
    /**
     * Type of the argument.
     */
    private final String type;

    /**
     * Name of the argument.
     */
    private final String name;

    /**
     * Creates a new Argument.
     * @param type String, must not be null and blank.
     * @param name String, must not be null and blank.
     */
    public Argument(final String type, final String name) {
        if (StringUtils.isAnyBlank(type, name)) {
            throw new IllegalArgumentException("Argument type and name must not be blank!");
        }
        this.type = type;
        this.name = name;
    }

    /**
     * Returns type of the Argument.
     * @return String, not null and not blank.
     */
    public String getType() {
        return type;
    }

    /**
     * Returns name of the Argument.
     * @return String, not null and not blank.
     */
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Argument argument = (Argument) o;

        if (!type.equals(argument.type)) {
            return false;
        }
        return name.equals(argument.name);
    }

    @Override
    public int hashCode() {
        return 31 * type.hashCode() + name.hashCode();
    }

    @Override
    public String toString() {
        return "Argument(type:" + type + ";name:" + name + ")";
    }
}
