package de.etgramlich.antlr.semanticmodel.scope;

import de.etgramlich.antlr.parser.listener.type.Rule;
import de.etgramlich.antlr.parser.listener.type.RuleList;
import de.etgramlich.antlr.util.SymbolTable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Build scopes from AST.
 */
public final class ScopeBuilder {
    private final List<Scope> scopes;

    @Contract(pure = true)
    public ScopeBuilder(@NotNull final RuleList ruleList) {
        scopes = new ArrayList<>();
        // ToDo: generate scopes from Tree
        for (Rule rule : ruleList.getRules()) {
            if (ruleList.isTerminal()) {
                // ToDo: Mark for replacing
                SymbolTable.add(rule.getLhs().getText(), rule);
            } else if (ruleList.isAlternative()) {
                // ToDo: Generate scope with methods of alternatives
            } else if (ruleList.isRepetition()) {
                // ToDo
            } else {
                throw new UnsupportedOperationException("Unexpected rule: " + ruleList.toString());
            }
        }
    }

    @NotNull
    @Contract(pure = true)
    public List<Scope> getScopes() {
        return Collections.unmodifiableList(scopes);
    }
}
