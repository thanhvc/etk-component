package org.etk.orm.api;

public class NoSuchPropertyException extends ORMException {
  public NoSuchPropertyException() {
  }

  public NoSuchPropertyException(String message) {
    super(message);
  }

  public NoSuchPropertyException(String message, Throwable cause) {
    super(message, cause);
  }

  public NoSuchPropertyException(Throwable cause) {
    super(cause);
  }
}