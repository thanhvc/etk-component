package org.etk.model.plugins.entity;

import org.etk.model.plugins.entity.type.SimpleTypeBinding;
import org.etk.orm.plugins.bean.ValueInfo;
import org.etk.orm.plugins.bean.ValueKind;
import org.etk.reflect.api.TypeInfo;

public class SimpleValueInfo<K extends ValueKind> extends ValueInfo {

  /** . */
  private final SimpleTypeBinding typeMapping;

  /** . */
  private final K valueKind;

  public SimpleValueInfo(
      TypeInfo declaredType,
      TypeInfo effectiveType,
      SimpleTypeBinding typeMapping,
      K valueKind) {
    super(declaredType, effectiveType);

    //
    this.typeMapping = typeMapping;
    this.valueKind = valueKind;
  }

  public SimpleTypeBinding getTypeMapping() {
    return typeMapping;
  }

  public K getValueKind() {
    return valueKind;
  }
}
