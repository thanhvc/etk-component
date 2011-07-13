package org.etk.orm.plugins.bean.type;


public class TypeConversionException extends RuntimeException {
  public TypeConversionException() {
  }

  public TypeConversionException(String message) {
    super(message);
  }

  public TypeConversionException(String message, Throwable cause) {
    super(message, cause);
  }

  public TypeConversionException(Throwable cause) {
    super(cause);
  }
}