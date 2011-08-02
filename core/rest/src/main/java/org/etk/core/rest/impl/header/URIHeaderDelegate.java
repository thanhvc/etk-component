package org.etk.core.rest.impl.header;

import java.net.URI;

public class URIHeaderDelegate extends AbstractHeaderDelegate<URI> {

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<URI> support() {
    return URI.class;
  }

  /**
   * {@inheritDoc}
   */
  public URI fromString(String header) {
    return URI.create(header);
  }

  /**
   * {@inheritDoc}
   */
  public String toString(URI uri) {
    return uri.toASCIIString();
  }

}

