package org.etk.core.rest.impl.header;

import javax.ws.rs.ext.RuntimeDelegate.HeaderDelegate;

public  abstract class AbstractHeaderDelegate<T> implements HeaderDelegate<T> {

  /**
   * @return the class which is supported by HeadeDelegate instance.
   */
  public abstract Class<T> support();

}
