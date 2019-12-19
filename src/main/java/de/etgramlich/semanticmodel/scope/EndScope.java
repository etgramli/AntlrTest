package de.etgramlich.semanticmodel.scope;

/**
 * Represents the ending scope that finally builds an object.
 *
 * All scopes, that can transition to the end state, must implement this!
 * (i.e. last element or element before last optional element)
 */
public interface EndScope extends Scope {
}
