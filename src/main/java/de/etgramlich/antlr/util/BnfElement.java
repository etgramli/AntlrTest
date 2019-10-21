package de.etgramlich.antlr.util;

public interface BnfElement {
    void accept(BnfTypeVisitor visitor);
}
