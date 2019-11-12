package de.etgramlich.antlr.util.visitor;

public interface BnfElement {
    void accept(BnfTypeVisitor visitor);
}
