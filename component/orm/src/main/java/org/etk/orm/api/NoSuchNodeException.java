package org.etk.orm.api;


public class NoSuchNodeException extends ORMException {

  public NoSuchNodeException() {
  }

  public NoSuchNodeException(String message) {
    super(message);
  }

  public NoSuchNodeException(String message, Throwable cause) {
    super(message, cause);
  }

  public NoSuchNodeException(Throwable cause) {
    super(cause);
  }
}
