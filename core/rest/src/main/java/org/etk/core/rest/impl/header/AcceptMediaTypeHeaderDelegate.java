package org.etk.core.rest.impl.header;

import javax.ws.rs.core.MediaType;

public class AcceptMediaTypeHeaderDelegate extends AbstractHeaderDelegate<AcceptMediaType> {

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<AcceptMediaType> support() {
    return AcceptMediaType.class;
  }

  /**
   * {@inheritDoc}
   */
  public AcceptMediaType fromString(String header) {
    if (header == null)
      throw new IllegalArgumentException();

    MediaType mediaType = MediaType.valueOf(header);

    return new AcceptMediaType(mediaType.getType(),
                                 mediaType.getSubtype(),
                                 mediaType.getParameters());

  }

  /**
   * {@inheritDoc}
   */
  public String toString(AcceptMediaType acceptedMediaType) {
    throw new UnsupportedOperationException("Accepted media type header used only for request.");
  }

}

