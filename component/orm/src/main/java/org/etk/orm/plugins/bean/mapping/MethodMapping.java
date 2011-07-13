package org.etk.orm.plugins.bean.mapping;

import org.etk.reflect.api.MethodInfo;

public abstract class MethodMapping {

  /** . */
  private final MethodInfo method;

  public MethodMapping(MethodInfo method) {
    this.method = method;
  }

  public MethodInfo getMethod() {
    return method;
  }

  public abstract void accept(MappingVisitor visitor);
}