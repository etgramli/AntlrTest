package de.etgramlich.dsl.generator;

import de.etgramlich.dsl.util.StringUtil;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.net.URL;
import java.util.List;


public final class ScalaInterfaceBuilder extends AbstractInterfaceBuilder {
    /**
     * Default file ending for scala source files.
     */
    private static final String DEFAULT_FILE_ENDING = ".scala";

    /**
     * File name of the scala interface template.
     */
    private static final String TEMPLATE_FILENAME = "scalaInterface.stg";

    /**
     * Name of the interface template.
     */
    private static final String INTERFACE_NAME = "templateInterface";

    /**
     * Loaded template file to be reused.
     */
    private static final STGroup ST_GROUP;

    static {
        final URL templateFileUrl = Thread.currentThread().getContextClassLoader().getResource(TEMPLATE_FILENAME);
        if (templateFileUrl == null) {
            throw new RuntimeException("Template fie javaInterface.stg could not be read!");
        }
        ST_GROUP = new STGroupFile(templateFileUrl);
    }

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

        final ST st = ST_GROUP.getInstanceOf(INTERFACE_NAME);
        st.add("package", getTargetPackage());
        st.add("interfaceName", anInterface.getName());
        st.add("methods", anInterface.getMethods());

        final List<String> parents = List.copyOf(anInterface.getParents());
        if (!parents.isEmpty()) {
            st.add("firstParent", parents.get(0));
            if (parents.size() > 1) {
                st.add("parents", parents.subList(1, parents.size()));
            }
        }

        return st.render();
    }
}
