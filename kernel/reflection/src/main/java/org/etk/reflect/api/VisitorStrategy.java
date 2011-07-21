package org.etk.reflect.api;


public interface VisitorStrategy<V extends Visitor<V, S>, S extends VisitorStrategy<V, S>> {
  void visit(TypeInfo type, V visitor);
}
