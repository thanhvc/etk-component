package org.etk.core.rest.impl;

/**
 * Should not be used by custom services. They have to use
 * {@link javax.ws.rs.WebApplicationException} instead. This Exception is
 * used as wrapper for exception that may occur during request processing.
 * 
 */
public class InternalException extends RuntimeException {

  /**
   * Serial Version UID.
   */
  private static final long serialVersionUID = -712006975338590407L;

  /**
   * @param s message
   * @param throwable cause
   */
  public InternalException(String s, Throwable throwable) {
    super(s, throwable);
  }

  /**
   * @param throwable cause
   */
  public InternalException(Throwable throwable) {
    super(throwable);
  }
}