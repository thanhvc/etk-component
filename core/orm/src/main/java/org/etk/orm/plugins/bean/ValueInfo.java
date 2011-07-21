package org.etk.orm.plugins.bean;

import org.etk.reflect.api.TypeInfo;

public abstract class ValueInfo {

  /** The property type as declared originally. */
  private final TypeInfo declaredType;

  /** The effective property type. */
  private final TypeInfo effectiveType;

  protected ValueInfo(TypeInfo declaredType, TypeInfo effectiveType) {
    if (declaredType == null) {
      throw new NullPointerException("No null declared type accepted");
    }
    if (effectiveType == null) {
      throw new NullPointerException("No null effective type accepted");
    }

    //
    this.declaredType = declaredType;
    this.effectiveType = effectiveType;
  }

  public TypeInfo getEffectiveType() {
    return effectiveType;
  }

  public TypeInfo getDeclaredType() {
    return declaredType;
  }
}
