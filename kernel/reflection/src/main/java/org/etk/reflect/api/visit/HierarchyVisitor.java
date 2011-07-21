package org.etk.reflect.api.visit;

import org.etk.reflect.api.ClassTypeInfo;
import org.etk.reflect.api.Visitor;
/**
 * Visits a {@link ClassTypeInfo} hierarchy.
 *
 */
public interface HierarchyVisitor<V extends HierarchyVisitor<V>> extends Visitor<V, HierarchyVisitorStrategy<V>> {

  boolean enter(ClassTypeInfo type);

  void leave(ClassTypeInfo type);

}
