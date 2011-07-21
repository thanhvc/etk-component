package org.etk.orm.plugins.common.jcr;

public class PathException extends Exception {

  public PathException() {
  }

  public PathException(String message) {
    super(message);
  }

  public PathException(String message, Throwable cause) {
    super(message, cause);
  }

  public PathException(Throwable cause) {
    super(cause);
  }
}
