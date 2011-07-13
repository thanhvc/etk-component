package org.etk.orm.api;

import javax.jcr.RepositoryException;

/**
 * Wraps an unexpected <tt>RepositoryException</tt> that is a checked exception. 
 *
 */
public final class UndeclaredRepositoryException extends ORMException {

  public UndeclaredRepositoryException() {
  }

  public UndeclaredRepositoryException(String message) {
    super(message);
  }

  public UndeclaredRepositoryException(String message, RepositoryException cause) {
    super(message, cause);
  }

  public UndeclaredRepositoryException(RepositoryException cause) {
    super(cause);
  }

  @Override
  public Throwable initCause(Throwable cause) {
    if (cause instanceof RepositoryException) {
      return super.initCause(cause);
    } else {
      throw new Error();
    }
  }

  @Override
  public RepositoryException getCause() {
    return (RepositoryException)super.getCause();
  }
}