package org.etk.core.rest.impl.header;

import javax.ws.rs.ext.RuntimeDelegate.HeaderDelegate;

public class StringHeaderDelegate extends AbstractHeaderDelegate<String> {

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<String> support() {
    return String.class;
  }

  /**
   * {@inheritDoc}
   */
  public String fromString(String value) {
    return value;
  }

  /**
   * {@inheritDoc}
   */
  public String toString(String value) {
    return value;
  }

}
