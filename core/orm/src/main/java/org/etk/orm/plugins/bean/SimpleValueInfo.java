package org.etk.orm.plugins.bean;

import org.etk.orm.plugins.bean.type.SimpleTypeMapping;
import org.etk.reflect.api.TypeInfo;

public class SimpleValueInfo<K extends ValueKind> extends ValueInfo {

  /** . */
  private final SimpleTypeMapping typeMapping;

  /** . */
  private final K valueKind;

  public SimpleValueInfo(
      TypeInfo declaredType,
      TypeInfo effectiveType,
      SimpleTypeMapping typeMapping,
      K valueKind) {
    super(declaredType, effectiveType);

    //
    this.typeMapping = typeMapping;
    this.valueKind = valueKind;
  }

  public SimpleTypeMapping getTypeMapping() {
    return typeMapping;
  }

  public K getValueKind() {
    return valueKind;
  }
}
