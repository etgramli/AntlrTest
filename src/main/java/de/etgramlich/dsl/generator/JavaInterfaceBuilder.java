package de.etgramlich.dsl.generator;

import de.etgramlich.dsl.util.StringUtil;

/**
 * Creates Java interfaces from a BnfRuleGraph and saves them to files.
 */
public final class JavaInterfaceBuilder extends AbstractInterfaceBuilder {
    /**
     * Java source code file ending.
     */
    private static final String DEFAULT_FILE_ENDING = ".java";

    /**
     * Name of the interface template.
     */
    private static final String INTERFACE_NAME = "javaInterface";

    /**
     * Creates InterfaceBuilder with a target (root) directory and package of the interfaces.
     *
     * @param targetDirectory Target directory to contain all interfaces.
     * @param targetPackage   Package of the interfaces (a subfolder will be created in target directory).
     * @param returnType Return type of ht´the end method, may be null, otherwise must not be blank.
     */
    public JavaInterfaceBuilder(final String targetDirectory, final String targetPackage, final String returnType) {
        super(targetDirectory, targetPackage, DEFAULT_FILE_ENDING, returnType);
    }

    /**
     * Render the passed interface as a Java Interface.
     *
     * @param anInterface Interface to be saved, must not be null.
     * @return Interface representation as String.
     */
    @Override
    String renderInterface(final Interface anInterface) {
        if (!StringUtil.startsWithUpperCase(anInterface.getName())) {
            throw new IllegalArgumentException("Interface name must start with an upper case letter! ("
                    + anInterface.getName() + ")");
        }

        return ST_GROUP.getInstanceOf(INTERFACE_NAME)
                .add("package", getTargetPackage())
                .add("interfaceName", anInterface.getName())
                .add("methods", anInterface.getMethods())
                .add("parents", anInterface.getParents())
                .render();
    }
}
