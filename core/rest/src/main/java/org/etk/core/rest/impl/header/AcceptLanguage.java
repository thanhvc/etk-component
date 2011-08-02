package org.etk.core.rest.impl.header;

import java.util.Locale;

import javax.ws.rs.ext.RuntimeDelegate;
import javax.ws.rs.ext.RuntimeDelegate.HeaderDelegate;


public class AcceptLanguage extends Language implements QualityValue {

  /**
   * Default accepted language, it minds any language is acceptable.
   */
  public static final AcceptLanguage                  DEFAULT  = new AcceptLanguage(new Locale("*"));

  /**
   * Quality value for 'accepted' HTTP headers, e. g. en-gb;0.9
   */
  private final float                                 qValue;

  /**
   * See {@link RuntimeDelegate#createHeaderDelegate(Class)}.
   */
  private static final HeaderDelegate<AcceptLanguage> DELEGATE =
    RuntimeDelegate.getInstance().createHeaderDelegate(AcceptLanguage.class);

  /**
   * Creates a new instance of AcceptedLanguage by parsing the supplied string.
   * 
   * @param header accepted language string
   * @return AcceptedLanguage
   */
  public static AcceptLanguage valueOf(String header) {
    return DELEGATE.fromString(header);
  }


  /**
   * Constructs new instance of accepted language with default quality value.
   * 
   * @param locale the language
   */
  public AcceptLanguage(Locale locale) {
    super(locale);
    qValue = DEFAULT_QUALITY_VALUE;
  }

  /**
   * Constructs new instance of accepted language with quality value.
   * 
   * @param locale the language
   * @param qValue quality value
   */
  public AcceptLanguage(Locale locale, float qValue) {
    super(locale);
    this.qValue = qValue;
  }

  // QualityValue

  /**
   * {@inheritDoc}
   */
  public float getQvalue() {
    return qValue;
  }

}
