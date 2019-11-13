package de.etgramlich.antlr.parser.type;

import de.etgramlich.antlr.util.visitor.BnfElement;
import de.etgramlich.antlr.util.visitor.BnfTypeVisitor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

public final class RuleList implements BnfType, BnfElement {
    private final List<Rule> rules;

    public RuleList(final Collection<Rule> rules) {
        this.rules = List.copyOf(rules);
    }

    @NotNull
    @Contract(pure = true)
    public List<Rule> getRules() {
        return rules;
    }

    public void saveInterfaces(final String rootDirectory, @NotNull final String pkg) {
        final String directory = rootDirectory + "/" + pkg.replace('.', '/');
        System.out.println(directory);
        for (Rule rule : rules) {
            final String filepath = directory + "/" + rule.getLhs().getText() + ".java";
            try (PrintWriter pw = new PrintWriter(filepath)) {
                pw.write(rule.buildInterface(pkg));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void accept(@NotNull BnfTypeVisitor visitor) {
        visitor.visit(this);
        rules.forEach(rule -> rule.accept(visitor));
    }
}
