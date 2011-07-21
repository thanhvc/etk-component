package org.etk.orm.api;


public class DuplicateNameException extends ORMException {
  public DuplicateNameException() {
  }

  public DuplicateNameException(String message) {
    super(message);
  }

  public DuplicateNameException(String message, Throwable cause) {
    super(message, cause);
  }

  public DuplicateNameException(Throwable cause) {
    super(cause);
  }
}