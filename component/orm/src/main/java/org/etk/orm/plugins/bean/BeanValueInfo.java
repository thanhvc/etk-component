package org.etk.orm.plugins.bean;

import org.etk.reflect.api.ClassTypeInfo;
import org.etk.reflect.api.TypeInfo;

public class BeanValueInfo extends ValueInfo {

  /** . */
  private final BeanInfo bean;

  /** . */
  private final ClassTypeInfo classType;

  public BeanValueInfo(TypeInfo declaredType, ClassTypeInfo classType, BeanInfo bean) {
    super(declaredType, classType);

    //
    this.classType = classType;
    this.bean = bean;
  }

  public ClassTypeInfo getClassType() {
    return classType;
  }

  public BeanInfo getBean() {
    return bean;
  }
}

