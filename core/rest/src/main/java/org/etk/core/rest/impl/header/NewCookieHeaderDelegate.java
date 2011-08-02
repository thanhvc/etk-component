package org.etk.core.rest.impl.header;

import javax.ws.rs.core.NewCookie;
import javax.ws.rs.ext.RuntimeDelegate.HeaderDelegate;


public class NewCookieHeaderDelegate extends AbstractHeaderDelegate<NewCookie> {

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<NewCookie> support() {
    return NewCookie.class;
  }

  /**
   * {@inheritDoc}
   */
  public NewCookie fromString(String header) {
    throw new UnsupportedOperationException("NewCookie used only for response headers.");
  }

  /**
   * {@inheritDoc}
   */
  public String toString(NewCookie cookie) {
    StringBuffer sb = new StringBuffer();
    sb.append(cookie.getName()).append('=').append(HeaderHelper.addQuotesIfHasWhitespace(cookie.getValue()));

    sb.append(';').append("Version=").append(cookie.getVersion());

    if (cookie.getComment() != null)
      sb.append(';').append("Comment=").append(HeaderHelper.addQuotesIfHasWhitespace(cookie.getComment()));

    if (cookie.getDomain() != null)
      sb.append(';').append("Domain=").append(HeaderHelper.addQuotesIfHasWhitespace(cookie.getDomain()));

    if (cookie.getPath() != null)
      sb.append(';').append("Path=").append(HeaderHelper.addQuotesIfHasWhitespace(cookie.getPath()));

    if (cookie.getMaxAge() != -1)
      sb.append(';').append("Max-Age=").append(HeaderHelper.addQuotesIfHasWhitespace("" + cookie.getMaxAge()));

    if (cookie.isSecure())
      sb.append(';').append("Secure");

    return sb.toString();
  }

}