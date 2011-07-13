package org.etk.orm.api;

import java.io.IOException;


public class ORMIOException extends ORMException {

  public ORMIOException() {
  }

  public ORMIOException(String message) {
    super(message);
  }

  public ORMIOException(String message, IOException rootCause) {
    super(message, rootCause);
  }

  public ORMIOException(IOException rootCause) {
    super(rootCause);
  }

  @Override
  public Throwable initCause(Throwable cause) {
    if (cause instanceof IOException) {
      return super.initCause(cause);
    } else {
      throw new Error();
    }
  }

  @Override
  public IOException getCause() {
    return (IOException)super.getCause();
  }
}
