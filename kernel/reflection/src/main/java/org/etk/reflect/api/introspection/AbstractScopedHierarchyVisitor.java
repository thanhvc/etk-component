package org.etk.reflect.api.introspection;

import org.etk.reflect.api.ClassTypeInfo;
import org.etk.reflect.api.visit.HierarchyVisitor;

abstract class AbstractScopedHierarchyVisitor implements HierarchyVisitor<AbstractScopedHierarchyVisitor> {

  /** . */
//  private final HierarchyScope hierarchyScope;

  /** . */
  private final ClassTypeInfo baseType;

  public AbstractScopedHierarchyVisitor(ClassTypeInfo baseType) {
    this.baseType = baseType;
//    this.hierarchyScope = hierarchyScope;
  }

  public boolean enter(ClassTypeInfo type) {
    return true;
  }

/*
  public boolean enter(ClassTypeInfo type) {
    if (type.getName().equals(Object.class.getName())) {
      return false;
    } else if (type == baseType) {
      return true;
    } else {
      switch (hierarchyScope) {
        case ALL:
          return true;
        case ANCESTORS:
          return type.getKind() == ClassKind.CLASS;
        case CLASS:
          return false;
        default:
          throw new AssertionError();
      }
    }
  }
*/
}
