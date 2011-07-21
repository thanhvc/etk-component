package org.etk.orm.api.format;

/**
 * Defines the default codec format that only performs validation of JCR names.
 *
 */
public class DefaultObjectFormatter implements ObjectFormatter {

  /** . */
  private static final DefaultObjectFormatter INSTANCE = new DefaultObjectFormatter();

  public static DefaultObjectFormatter getInstance() {
    return INSTANCE;
  }

  public String decodeNodeName(FormatterContext context, String internalName) {
    return internalName;
  }

  public String encodeNodeName(FormatterContext context, String externalName) {
    return externalName;
  }

  public String decodePropertyName(FormatterContext context, String internalName) {
    return internalName;
  }

  public String encodePropertyName(FormatterContext context, String externalName) {
    return externalName;
  }
}
