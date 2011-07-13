package org.etk.orm.apt;

/**
 * Visitor pattern for a java class/interface type hierarchy. The visitor strategy
 * for a specified type is recursive
 * <ol>
 *   <li>Invoke the method {@link #enter(Class)} with the type. When the method returns false it ends the visit.</li>
 *   <li>Continue the visit recursively on the type superclass when it does have one.</li>
 *   <li>Continue the visit recursively on each implemented interfaces.</li>
 *   <li>Invoke the method {@link #leave(Class)} to signal that visit of the type is terminated.</li>
 * </ol>
 *
 */
public class TypeHierarchyVisitor {

  public void accept(Class type) {
    _accept(type);
  }

  private boolean _accept(Class type) {
    if (enter(type)) {
      if (type.isInterface()) {
        for (Class superInterface : type.getInterfaces()) {
          if (!_accept(superInterface)) {
            return false;
          }
        }
      } else {
        Class superType = type.getSuperclass();
        if (superType != null) {
          if (!_accept(superType)) {
            return false;
          }
        }
        for (Class implementedInterface : type.getInterfaces()) {
          if (!_accept(implementedInterface)) {
            return false;
          }
        }
      }

      //
      leave(type);
    }
    return true;
  }

  /**
   * Subclass to control the visitor.
   *
   * @param type the visited type
   * @return true if the type shall be visited
   */
  protected boolean enter(Class type) {
    return true;
  }

  /**
   * Signal that the visit of the type is terminated.
   *
   * @param type the type
   */
  protected void leave(Class type) {
  }
}
