package org.etk.reflect.api.visit;

import org.etk.reflect.api.ClassTypeInfo;
import org.etk.reflect.api.ParameterizedTypeInfo;
import org.etk.reflect.api.TypeInfo;
import org.etk.reflect.api.VisitorStrategy;

public abstract class HierarchyVisitorStrategy<V extends HierarchyVisitor<V>> implements VisitorStrategy<V, HierarchyVisitorStrategy<V>> {

  public final void visit(TypeInfo type, V visitor) {
    if (type instanceof ClassTypeInfo) {
      ClassTypeInfo classType = (ClassTypeInfo)type;
      if (accept(classType)) {
        if (visitor.enter(classType)) {
          TypeInfo superType = classType.getSuperType();
          if (superType != null) {
            visit(superType, visitor);
          }
          for (TypeInfo implementedInterface : classType.getInterfaces()) {
            visit(implementedInterface, visitor);
          }
          visitor.leave(classType);
        }
        leave(classType);
      }
    } else if (type instanceof ParameterizedTypeInfo) {
      visit(((ParameterizedTypeInfo)type).getRawType(), visitor);
    } else {
    }
  }

  /**
   * Controls wether or not a type will be visited. The method implementation returns true and can be overriden.
   *
   * @param type the type to accept or refuse
   * @return true if the type should be visited
   */
  protected boolean accept(ClassTypeInfo type) {
    return true;
  }

  /**
   * Signals that a type has been visited. This method will only be called for types which have been accepted
   * by the {@link #accept(org.reflext.api.ClassTypeInfo)} method. The default implementation has an empty body.
   *
   * @param type the visited type
   */
  protected void leave(ClassTypeInfo type) {
  }
}

