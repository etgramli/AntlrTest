package de.etgramlich.dsl.generator;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a method. Used in an intermediate step before writing interfaces to files.
 */
public final class Method {
    /**
     * Return type of the Method.
     */
    private final String returnType;

    /**
     * Name of the Method.
     */
    private final String name;

    /**
     * List of Arguments.
     */
    private final List<Argument> arguments;

    // ToDo: Add method body

    /**
     * Creates new Method from return type, name and arguments.
     * @param returnType String, must not be blank.
     * @param name String, must not be blank.
     * @param arguments List of Arguments, must not be empty.
     */
    public Method(final String returnType, final String name, final List<Argument> arguments) {
        if (StringUtils.isBlank(returnType)) {
            throw new IllegalArgumentException("Return type must not be blank!");
        }
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Method name must not be blank!");
        }
        this.returnType = returnType;
        this.name = name;
        this.arguments = arguments == null ? Collections.emptyList() : List.copyOf(arguments);
    }

    /**
     * Creates a Method from a return type, name and single argument.
     * @param returnType String, must not be blank.
     * @param name String, must not be blank.
     * @param argument Argument object, must not be null.
     */
    public Method(final String returnType, final String name, final Argument argument) {
        this(returnType, name, List.of(argument));
    }

    /**
     * Creates new Method from return type and name.
     * @param returnType String, must not be blank.
     * @param name String, must not be blank.
     */
    public Method(final String returnType, final String name) {
        this(returnType, name, Collections.emptyList());
    }

    /**
     * Returns return type.
     * @return String, not blank.
     */
    public String getReturnType() {
        return returnType;
    }

    /**
     * Returns name.
     * @return String, not blank.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns Arguments list.
     * @return List, not null, may be empty.
     */
    public List<Argument> getArguments() {
        return arguments;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Method method = (Method) o;

        if (!returnType.equals(method.returnType)) {
            return false;
        }
        if (!name.equals(method.name)) {
            return false;
        }
        return arguments.equals(method.arguments);
    }

    @Override
    public int hashCode() {
        int result = returnType.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + arguments.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Method(returnType:");
        sb.append(returnType).append(";name:").append(name).append(";");
        for (Iterator<Argument> argument = arguments.iterator(); argument.hasNext();) {
            sb.append(argument.next().toString());
            if (argument.hasNext()) {
                sb.append(",");
            }
        }
        return sb.toString();
    }
}
