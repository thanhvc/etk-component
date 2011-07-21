package org.etk.orm.plugins.bean.mapping;

import org.etk.reflect.api.MethodInfo;

public class CreateMapping extends MethodMapping {

  /** . */
  private final BeanMapping beanMapping;

  public CreateMapping(MethodInfo method, BeanMapping bean) {
    super(method);

    //
    this.beanMapping = bean;
  }

  public BeanMapping getBeanMapping() {
    return beanMapping;
  }

  @Override
  public void accept(MappingVisitor visitor) {
    visitor.visit(this);
  }
}