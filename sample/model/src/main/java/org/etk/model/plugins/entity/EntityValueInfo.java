package org.etk.model.plugins.entity;

import org.etk.orm.plugins.bean.ValueInfo;
import org.etk.reflect.api.ClassTypeInfo;
import org.etk.reflect.api.TypeInfo;

public class EntityValueInfo  extends ValueInfo {

  /** . */
  private final EntityInfo bean;

  /** . */
  private final ClassTypeInfo classType;

  public EntityValueInfo(TypeInfo declaredType, ClassTypeInfo classType, EntityInfo bean) {
    super(declaredType, classType);

    //
    this.classType = classType;
    this.bean = bean;
  }

  public ClassTypeInfo getClassType() {
    return classType;
  }

  public EntityInfo getBean() {
    return bean;
  }
}