package org.etk.core.rest.impl.header;

import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.RuntimeDelegate;
import javax.ws.rs.ext.RuntimeDelegate.HeaderDelegate;

public class AcceptMediaType extends MediaType implements QualityValue {

  /**
   * Default accepted media type, it minds any content type is acceptable.
   */
  public static final AcceptMediaType                  DEFAULT  = new AcceptMediaType("*", "*");

  /**
   * Quality value for 'accepted' HTTP headers, e. g. text/plain;q=0.9
   */
  private final float                                  qValue;

  /**
   * See {@link RuntimeDelegate#createHeaderDelegate(Class)}.
   */
  private static final HeaderDelegate<AcceptMediaType> DELEGATE =
      RuntimeDelegate.getInstance().createHeaderDelegate(AcceptMediaType.class);

  /**
   * Creates a new instance of AcceptedMediaType by parsing the supplied string.
   * 
   * @param header accepted media type string
   * @return AcceptedMediaType
   */
  public static AcceptMediaType valueOf(String header) {
    return DELEGATE.fromString(header);
  }

  /**
   * Creates a new instance of MediaType, both type and sub-type are wildcards
   * and set quality value to default quality value.
   */
  public AcceptMediaType() {
    super();
    this.qValue = DEFAULT_QUALITY_VALUE;
  }

  /**
   * Constructs AcceptedMediaType with supplied quality value. If map parameters
   * is null or does not contains value with key 'q' then default quality value
   * will be used.
   * 
   * @param type media type
   * @param subtype media sub-type
   * @param parameters addition header parameters
   */
  public AcceptMediaType(String type, String subtype, Map<String, String> parameters) {
    super(type, subtype, parameters);
    String qstring;
    if (parameters != null && (qstring = parameters.get(QVALUE)) != null)
      this.qValue = HeaderHelper.parseQualityValue(qstring);
    else
      this.qValue = DEFAULT_QUALITY_VALUE;
  }

  /**
   * Constructs AcceptedMediaType with default quality value.
   * 
   * @param type media type
   * @param subtype media sub-type
   */
  public AcceptMediaType(String type, String subtype) {
    super(type, subtype);
    this.qValue = DEFAULT_QUALITY_VALUE;
  }

  // QualityValue
  
  /**
   * {@inheritDoc}
   */
  public float getQvalue() {
    return qValue;
  }

}