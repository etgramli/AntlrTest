package de.etgramlich.util.graph;

import de.etgramlich.util.graph.type.Scope;
import de.etgramlich.util.graph.type.node.Node;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jgrapht.Graph;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public final class InterfaceBuilder {
    private static final String INTERFACE_FILENAME = "src/main/resources/ebnf.stg";
    private static final String INTERFACE_NAME = "templateInterface";
    private static final String DEFAULT_FILE_ENDING = ".java";
    private static final STGroup stgroup = new STGroupFile(INTERFACE_FILENAME);

    private final String targetDirectory;
    private final String targetPackage;
    private final String packageDirectory;

    @Contract(pure = true)
    public InterfaceBuilder(@NotNull final String targetDirectory, @NotNull final String targetPackage) {
        this.targetDirectory = targetDirectory;
        this.targetPackage = targetPackage;
        this.packageDirectory = targetPackage.replaceAll("\\.", "/");
    }

    public void saveInterfaces(final Graph graph) {
        // ToDo
    }

    public void saveInterface(@NotNull final Scope scope, @NotNull final Node node) {
        final ST st = stgroup.getInstanceOf(INTERFACE_NAME);
        st.add("package", targetPackage);
        // ToDo

        final String filePath = targetDirectory + packageDirectory + scope.getName() + DEFAULT_FILE_ENDING;
        try (PrintWriter out = new PrintWriter(scope.getName() + ".java")) {
            out.write(st.render());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
