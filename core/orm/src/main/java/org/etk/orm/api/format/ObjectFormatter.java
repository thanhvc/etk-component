package org.etk.orm.api.format;


/**
 * The object formatter defines an interface used to filter the naming of the jcr nodes and properties.
 *
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public interface ObjectFormatter {

  /**
   * Converts a jcr node name to an entity name.
   *
   * @param context the context
   * @param internalName the jcr node name
   * @return the entity name
   */
  String decodeNodeName(FormatterContext context, String internalName);

  /**
   * Converts an entity name to a jcr node name.
   *
   * @param context the context
   * @param externalName the entity name
   * @return the jcr node name
   * @throws IllegalArgumentException if the name cannot be converted due to its nature
   * @throws NullPointerException if the name would convert to a value meaning nullity
   */
  String encodeNodeName(FormatterContext context, String externalName) throws IllegalArgumentException, NullPointerException;

  /**
   * Converts a jcr property name to an entity property name. If the property cannot be decoded
   * then null must be returned.
   *
   * @param context the context
   * @param internalName the jcr property name
   * @return the entity property name
   */
  // String decodePropertyName(FormatterContext context, String internalName);

  /**
   * Converts an entity property name to a jcr property name.
   *
   * @param context the context
   * @param externalName the entity property name
   * @return the jcr property name
   * @throws IllegalArgumentException if the name cannot be converted due to its nature
   * @throws NullPointerException if the name would convert to a value meaning nullity
   */
  // String encodePropertyName(FormatterContext context, String externalName) throws IllegalArgumentException, NullPointerException;

}