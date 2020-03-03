package de.etgramlich.graph.type;

import de.etgramlich.util.StringSupplier;

import java.util.function.Supplier;

public final class ScopeSupplier implements Supplier<Scope> {
    /**
     * Prefix for the scope names.
     */
    private static final String PREFIX = "Scope_";

    /**
     * Supplier to generate numbered names for the scopes.
     */
    private StringSupplier supplier = new StringSupplier(PREFIX);

    @Override
    public Scope get() {
        return new Scope(supplier.get());
    }
}
