package de.etgramlich.graph.type;

import java.util.function.Supplier;

public final class ScopeProvider implements Supplier<Scope> {

    /**
     * Counter used to number all generated scope.
     */
    private int counter = 0;

    @Override
    public Scope get() {
        return new Scope("Scope_" + counter++);
    }
}
