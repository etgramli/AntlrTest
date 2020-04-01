package de.etgramlich.dsl.graph.type;

import de.etgramlich.dsl.util.StringSupplier;

import java.util.function.Supplier;

public final class ScopeSupplier implements Supplier<Scope> {
    /**
     * Prefix for the scope names.
     */
    private static final String PREFIX = "Scope";

    /**
     * Supplier to generate numbered names for the scopes.
     */
    private final StringSupplier supplier = new StringSupplier(PREFIX);

    @Override
    public Scope get() {
        return new Scope(supplier.get());
    }
}
