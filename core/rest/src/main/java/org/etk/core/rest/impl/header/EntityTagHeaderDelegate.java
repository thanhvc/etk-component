package org.etk.core.rest.impl.header;

import javax.ws.rs.core.EntityTag;
import javax.ws.rs.ext.RuntimeDelegate.HeaderDelegate;


public class EntityTagHeaderDelegate extends AbstractHeaderDelegate<EntityTag> {

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<EntityTag> support() {
    return EntityTag.class;
  }

  /**
   * {@inheritDoc}
   */
  public EntityTag fromString(String header) {
    if (header == null)
      throw new IllegalArgumentException();

    boolean isWeak = header.startsWith("W/") ? true : false;

    String value;
    // cut 'W/' prefix if exists
    if (isWeak)
      value = header.substring(2);
    else
      value = header;
    // remove quotes
    value = value.substring(1, value.length() - 1);
    value = HeaderHelper.filterEscape(value);

    return new EntityTag(value, isWeak);
  }

  /**
   * {@inheritDoc}
   */
  public String toString(EntityTag entityTag) {
    StringBuffer sb = new StringBuffer();
    if (entityTag.isWeak())
      sb.append('W').append('/');

    sb.append('"');
    HeaderHelper.appendEscapeQuote(sb, entityTag.getValue());
    sb.append('"');

    return sb.toString();
  }

}

