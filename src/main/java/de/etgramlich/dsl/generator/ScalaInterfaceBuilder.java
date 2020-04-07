package de.etgramlich.dsl.generator;

import de.etgramlich.dsl.util.StringUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

public final class ScalaInterfaceBuilder extends AbstractInterfaceBuilder {
    /**
     * Default file ending for scala source files.
     */
    private static final String DEFAULT_FILE_ENDING = ".scala";

    /**
     * Name of the interface template.
     */
    private static final String INTERFACE_NAME = "scalaTrait";

    /**
     * Creates ScalaInterfaceBuilder with a target (root) directory and package of the interfaces.
     *
     * @param targetDirectory Target directory to contain all interfaces.
     * @param targetPackage   Package of the interfaces (a subfolder will be created in target directory).
     */
    public ScalaInterfaceBuilder(final String targetDirectory, final String targetPackage) {
        super(targetDirectory, targetPackage, DEFAULT_FILE_ENDING);
    }

    @Override
    String renderInterface(final Interface anInterface) {
        if (!StringUtil.startsWithUpperCase(anInterface.getName())) {
            throw new IllegalArgumentException("Interface name must start with an upper case letter!");
        }

        return ST_GROUP.getInstanceOf(INTERFACE_NAME)
                .add("package", getTargetPackage())
                .add("interfaceName", anInterface.getName())
                .add("methods", replaceVoidReturnType(anInterface.getMethods()))
                .add("parents", anInterface.getParents())
                .render();
    }

    private static Set<Method> replaceVoidReturnType(final Collection<Method> methods) {
        final Set<Method> methodSet = new HashSet<>(methods.size());
        for (Method method : methods) {
            if (method.getReturnType().equals("void")) {
                methodSet.add(new Method("Unit", method.getName(), method.getArguments()));
            } else {
                methodSet.add(method);
            }
        }
        return Collections.unmodifiableSet(methodSet);
    }
}
