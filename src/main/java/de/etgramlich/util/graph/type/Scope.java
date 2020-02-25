package de.etgramlich.util.graph.type;

import org.apache.commons.lang3.StringUtils;

public final class Scope {

    /**
     * Identifier for this scope.
     */
    private final String name;

    /**
     * Creates a scope with the given name as identifier.
     * @param name String, must not be null and blank.
     */
    public Scope(final String name) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Scope name must not be blank!");
        }
        this.name = name;
    }

    /**
     * Returns the name of the scope.
     * @return String, not null, not empty.
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

        Scope scope = (Scope) o;

        return name.equals(scope.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
