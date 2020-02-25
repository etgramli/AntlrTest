package de.etgramlich.generator;

import org.apache.commons.lang3.StringUtils;

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
}
