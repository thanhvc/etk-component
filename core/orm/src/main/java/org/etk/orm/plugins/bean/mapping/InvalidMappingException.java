package org.etk.orm.plugins.bean.mapping;

import org.etk.reflect.api.ClassTypeInfo;

public class InvalidMappingException extends RuntimeException {
  private final ClassTypeInfo type;

  public InvalidMappingException(ClassTypeInfo type) {
    this.type = type;
  }

  public InvalidMappingException(ClassTypeInfo type, String message) {
    super(message);

    //
    this.type = type;
  }

  public InvalidMappingException(ClassTypeInfo type, String message, Throwable cause) {
    super(message, cause);

    //
    this.type = type;
  }

  public InvalidMappingException(ClassTypeInfo type, Throwable cause) {
    super(cause);

    //
    this.type = type;
  }

  @Override
  public String getMessage() {
    String superMsg = super.getMessage();
    if (superMsg != null) {
      return "Cannot build type " + type.getName() + ":" + superMsg;
    } else {
      return "Cannot build type " + type.getName();
    }
  }

  public ClassTypeInfo getType() {
    return type;
  }
}