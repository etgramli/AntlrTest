package de.etgramlich.antlr.parser.listener.bnf.type;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

public final class RuleList implements BnfType {
    private final List<Rule> rules;

    public RuleList(final Collection<Rule> rules) {
        this.rules = List.copyOf(rules);
    }

    @NotNull
    @Contract(pure = true)
    public List<Rule> getRules() {
        return rules;
    }

    public void saveInterfaces(final String directory) {
        for (Rule rule : rules) {
            try (PrintWriter pw = new PrintWriter(directory + "/" + rule.getLhs().getText())) {
                pw.write(rule.buildInterface());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
