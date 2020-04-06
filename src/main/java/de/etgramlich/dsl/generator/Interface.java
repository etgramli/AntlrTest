package de.etgramlich.dsl.generator;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

/**
 * Represents an interface. Used in an intermediate step before writing interfaces to files.
 */
public final class Interface {
    /**
     * Name of the interface.
     */
    private String name;

    /**
     * Super type of the interfaces, may be null.
     */
    private final String parent;

    /**
     * Methods of the interface, may be empty.
     */
    private final Set<Method> methods;

    /**
     * Creates an interface from name, List of super types and collection of methods.
     * @param name Name of the interface as string, must not be blank.
     * @param parent Super type as sting, may be null, if not mus not be blank.
     * @param methods Collection of Methods, must not be null, may be empty.
     */
    public Interface(final String name, final String parent, final Collection<Method> methods) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Interface name must not be blank!");
        }
        if (parent != null && StringUtils.isBlank(parent)) {
            throw new IllegalArgumentException("Parent must be null or not blank!");
        }
        if (methods == null) {
            throw new IllegalArgumentException("Methods must not be null!");
        }
        this.name = name;
        this.parent = parent;
        this.methods = Set.copyOf(methods);
    }

    /**
     * Returns the name of the interface.
     * @return String, not blank.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the interface to the new one.
     * @param newName String, must not be blank.
     */
    public void setName(final String newName) {
        if (StringUtils.isBlank(newName)) {
            throw new IllegalArgumentException("New name must not be blank!");
        }
        name = newName;
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
    public String getParent() {
        return parent;
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
        if (!Objects.equals(parent, that.parent)) {
            return false;
        }
        return methods.equals(that.methods);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        result = 31 * result + methods.hashCode();
        return result;
    }
}
