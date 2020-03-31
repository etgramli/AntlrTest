package de.etgramlich.dsl.generator;

import de.etgramlich.dsl.util.StringUtil;
import org.stringtemplate.v4.ST;

import java.util.List;


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

        final ST st = ST_GROUP.getInstanceOf(INTERFACE_NAME)
                .add("package", getTargetPackage())
                .add("interfaceName", anInterface.getName())
                .add("methods", anInterface.getMethods());

        final List<String> parents = List.copyOf(anInterface.getParents());
        if (!parents.isEmpty()) {
            st.add("firstParent", parents.get(0)).add("parents", parents.subList(1, parents.size()));
        }

        return st.render();
    }
}
