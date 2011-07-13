package org.etk.orm.plugins.bean.mapping;

import org.etk.reflect.api.MethodInfo;

public class DestroyMapping extends MethodMapping {

  public DestroyMapping(MethodInfo method) {
    super(method);
  }

  @Override
  public void accept(MappingVisitor visitor) {
    visitor.visit(this);
  }
}