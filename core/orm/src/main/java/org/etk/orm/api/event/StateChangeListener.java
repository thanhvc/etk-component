package org.etk.orm.api.event;


/**
 * Allows for state change monitoring when the state of an object is changed.
 *
 */
public interface StateChangeListener extends EventListener {

  /**
   * The value of a property changed
   *
   * @param id the object id
   * @param o the object
   * @param propertyName the property name
   * @param propertyValue the property value
   */
  void propertyChanged(String id, Object o, String propertyName, Object propertyValue);


}