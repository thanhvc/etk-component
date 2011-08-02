package org.etk.core.rest.impl.header;

import java.text.ParseException;
import java.util.Locale;
import java.util.Map;

public class AcceptLanguageHeaderDelegate extends AbstractHeaderDelegate<AcceptLanguage> {

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<AcceptLanguage> support() {
    return AcceptLanguage.class;
  }

  /**
   * {@inheritDoc}
   */
  public AcceptLanguage fromString(String header) {
    if (header == null)
      throw new IllegalArgumentException();
    
    try {
      header = HeaderHelper.removeWhitespaces(header);
      String tag;
      Map<String, String> m = null;

      int p = header.indexOf(';');
      if (p != -1 && p < header.length() - 1) { // header has quality value
        tag = header.substring(0, p);
        m = new HeaderParameterParser().parse(header);
      } else { // no quality value
        tag = header;
      }

      p = tag.indexOf('-');
      String primaryTag;
      String subTag = null;

      if (p != -1 && p < tag.length() - 1) { // has sub-tag
        primaryTag = tag.substring(0, p);
        subTag = tag.substring(p + 1);
      } else { // no sub-tag
        primaryTag = tag;
      }

      if (m == null) // no quality value
        return new AcceptLanguage(new Locale(primaryTag, subTag != null ? subTag : ""));
      else
        return new AcceptLanguage(new Locale(primaryTag, subTag != null ? subTag : ""),
                                    HeaderHelper.parseQualityValue(m.get(QualityValue.QVALUE)));

    } catch (ParseException e) {
      throw new IllegalArgumentException("Accept language header malformed");
    }
  }

  /**
   * {@inheritDoc}
   */
  public String toString(AcceptLanguage language) {
    throw new UnsupportedOperationException("Accepted language header used only for request.");
  }

}

