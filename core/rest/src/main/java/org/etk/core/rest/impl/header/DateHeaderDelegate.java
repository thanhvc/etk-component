package org.etk.core.rest.impl.header;

import java.util.Date;

public class DateHeaderDelegate extends AbstractHeaderDelegate<Date> {

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<Date> support() {
    return Date.class;
  }

  /**
   * Parse date header, header string must be in one of HTTP-date format see
   * {@link <a
   * href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html#sec3.3.1"
   * >HTTP/1.1 documentation</a>} otherwise IllegalArgumentException will be
   * thrown. {@inheritDoc}
   */
  public Date fromString(String header) {
    return HeaderHelper.parseDateHeader(header);
  }

  /**
   * Represents {@link Date} as String in format of RFC 1123 {@inheritDoc} .
   */
  public String toString(Date date) {
    return HeaderHelper.getDateFormats().get(0).format(date);
  }

}
