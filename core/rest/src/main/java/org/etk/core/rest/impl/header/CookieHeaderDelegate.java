package org.etk.core.rest.impl.header;

import java.util.List;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.ext.RuntimeDelegate.HeaderDelegate;


public class CookieHeaderDelegate extends AbstractHeaderDelegate<Cookie> {

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<Cookie> support() {
    return Cookie.class;
  }

  /**
   * {@inheritDoc}
   */
  public Cookie fromString(String header) {
    if (header == null)
      throw new IllegalArgumentException();
    
    List<Cookie> l = HeaderHelper.parseCookies(header);
    if (l.size() > 0) // waiting for one cookie
      return l.get(0);

    return null;
  }

  /**
   * {@inheritDoc}
   */
  public String toString(Cookie cookie) {
    StringBuilder sb = new StringBuilder();

    sb.append("$Version=").append(cookie.getVersion()).append(';');

    sb.append(cookie.getName()).append('=').append(HeaderHelper.addQuotesIfHasWhitespace(cookie.getValue()));

    if (cookie.getDomain() != null)
      sb.append(';').append("$Domain=").append(HeaderHelper.addQuotesIfHasWhitespace(cookie.getDomain()));

    if (cookie.getPath() != null)
      sb.append(';').append("$Path=").append(HeaderHelper.addQuotesIfHasWhitespace(cookie.getPath()));

    return sb.toString();
  }

}

