package org.etk.orm.plugins.bean.mapping;

import org.etk.reflect.api.ClassTypeInfo;
import org.etk.reflect.api.MethodInfo;

public class FindByIdMapping extends MethodMapping {

  /** . */
  private final ClassTypeInfo type;

  public FindByIdMapping(MethodInfo method, ClassTypeInfo type) {
    super(method);

    //
    this.type = type;
  }

  public ClassTypeInfo getType() {
    return type;
  }

  @Override
  public void accept(MappingVisitor visitor) {
    visitor.visit(this);
  }
}