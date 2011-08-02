package org.etk.core.rest.impl.header;


public class AcceptToken extends Token implements QualityValue {

  /**
   * Quality value factor.
   */
  private final float qValue;

  /**
   * Create AcceptToken with default quality value 1.0 .
   * 
   * @param token a token
   */
  public AcceptToken(String token) {
    super(token);
    qValue = DEFAULT_QUALITY_VALUE;
  }

  /**
   * Create AcceptToken with specified quality value.
   * @param token a token
   * @param qValue a quality value
   */
  public AcceptToken(String token, float qValue) {
    super(token);
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
