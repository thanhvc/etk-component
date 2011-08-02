package org.etk.core.rest.impl.header;

/**
 * Implementation of this interface is useful for sort accepted media type and
 * languages by quality factor. For example see
 * {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.1">HTTP/1.1 documentation</a>}.
 * 
 */
public interface QualityValue {

  /**
   * Default quality value. It should be used if quality value is not specified
   * in accept token.
   */
  public static final float DEFAULT_QUALITY_VALUE = 1.0F; 
  
  /**
   * Quality value.
   */
  public static final String QVALUE = "q"; 
  
  /**
   * @return value of quality parameter
   */
  float getQvalue();

}
