package org.etk.orm.api;

public abstract class ORMException extends RuntimeException {

  public ORMException() {
  }

  public ORMException(String message) {
    super(message);
  }

  public ORMException(String message, Throwable cause) {
    super(message, cause);
  }

  public ORMException(Throwable cause) {
    super(cause);
  }
}
