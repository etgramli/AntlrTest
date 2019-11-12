package de.etgramlich.antlr.semanticmodel.scope;

import de.etgramlich.antlr.parser.listener.type.Rule;
import de.etgramlich.antlr.parser.listener.type.RuleList;
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
    public ScopeBuilder(final List<RuleList> ruleLists) {
        scopes = new ArrayList<>();
        // ToDo: generate scopes from Tree
        for (RuleList ruleList : ruleLists) {
            for (Rule rule : ruleList.getRules()) {
                // ToDo
            }
        }
    }

    @NotNull
    @Contract(pure = true)
    public List<Scope> getScopes() {
        return Collections.unmodifiableList(scopes);
    }
}
