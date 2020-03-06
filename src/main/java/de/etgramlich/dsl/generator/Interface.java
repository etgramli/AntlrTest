package de.etgramlich.dsl.generator;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * Represents an interface. Used in an intermediate step before writing interfaces to files.
 */
public final class Interface {
    /**
     * Name of the interface.
     */
    private final String name;

    /**
     * Super types of the interfaces, may be empty.
     */
    private final Set<String> parents;

    /**
     * Methods of the interface, may be empty.
     */
    private final Set<Method> methods;

    /**
     * Creates interface from name, List of super types and collection of methods.
     * @param name Name of the interface as string, must not be blank.
     * @param parents Collection of super types as sting, may be null or empty.
     * @param methods Collection of Methods, may be null or empty.
     */
    public Interface(final String name, final Collection<String> parents, final Collection<Method> methods) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Interface name must not be blank!");
        }
        this.name = name;
        this.parents = parents == null ? Collections.emptySet() : Set.copyOf(parents);
        this.methods = methods == null ? Collections.emptySet() : Set.copyOf(methods);
    }

    /**
     * Returns the name of the interface.
     * @return String, not blank.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns (Set of) methods.
     * @return Collection, not null, may be empty.
     */
    public Collection<Method> getMethods() {
        return methods;
    }

    /**
     * Returns (Set of) super types.
     * @return Collection, not null, may be empty.
     */
    public Set<String> getParents() {
        return parents;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Interface that = (Interface) o;

        if (!name.equals(that.name)) {
            return false;
        }
        if (!parents.equals(that.parents)) {
            return false;
        }
        return methods.equals(that.methods);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + parents.hashCode();
        result = 31 * result + methods.hashCode();
        return result;
    }
}
