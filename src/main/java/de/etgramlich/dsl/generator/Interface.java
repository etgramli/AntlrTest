package de.etgramlich.dsl.generator;

import de.etgramlich.dsl.util.CollectionUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents an interface. Used in an intermediate step before writing interfaces to files.
 */
public final class Interface {
    /**
     * Name of the interface.
     */
    private String name;

    /**
     * Super types of the interfaces, may be empty.
     */
    private final Set<String> parents;

    /**
     * Methods of the interface, may be empty.
     */
    private final Set<Method> methods;

    /**
     * Creates an interface from name, List of super types and collection of methods.
     * @param name Name of the interface as string, must not be blank.
     * @param parents Collection of super types as sting, must noe be null, may be empty.
     * @param methods Collection of Methods, must not be null, may be empty.
     */
    public Interface(final String name, final Collection<String> parents, final Collection<Method> methods) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Interface name must not be blank!");
        }
        if (parents == null) {
            throw new IllegalArgumentException("Parents must not be null!");
        }
        if (methods == null) {
            throw new IllegalArgumentException("Methods must not be null!");
        }
        this.name = name;
        this.parents = Set.copyOf(parents);
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Interface(");
        sb.append("name:").append(name).append(';');
        sb.append("parents(").append(String.join(",", parents));
        sb.append("), methods(");
        for (Iterator<Method> method = methods.iterator(); method.hasNext();) {
            sb.append(method.next().toString());
            if (method.hasNext()) {
                sb.append(',');
            }
        }
        sb.append("))");
        return sb.toString();
    }
}
