package org.etk.core.rest.impl.header;

import java.util.Locale;

import javax.ws.rs.ext.RuntimeDelegate.HeaderDelegate;

public class LocaleHeaderDelegate extends AbstractHeaderDelegate<Locale> {

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<Locale> support() {
    return Locale.class;
  }

  /**
   * {@inheritDoc}
   */
  public Locale fromString(String header) {
    if (header == null)
      throw new IllegalArgumentException();
    
    header = HeaderHelper.removeWhitespaces(header);
    int p;
    // Can be set multiple content language, the take first one
    if ((p = header.indexOf(',')) > 0)
      header = header.substring(0, p);
    
    p = header.indexOf('-');
    if (p != -1 && p < header.length() - 1)
      return new Locale(header.substring(0, p), header.substring(p + 1));
    else
      return new Locale(header);
  }

  /**
   * {@inheritDoc}
   */
  public String toString(Locale locale) {
    String lan = locale.getLanguage();
    // For output if language does not set correctly then ignore it.
    if ("".equals(lan) || "*".equals(lan))
      return null;

    String con = locale.getCountry();
    if ("".equals(lan))
      return lan.toLowerCase();

    return lan.toLowerCase() + "-" + con.toLowerCase();
  }

}

