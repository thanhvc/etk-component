package org.etk.orm.api.event;


/**
 * Enables to be aware of the life cycle of the object managed by chromattic with respect to the underlying
 * JCR session. Those life cycle callbacks does not guarantees that they will translate to operations
 * with the persitence storage as there are not guarantees that the session will be saved.
 *
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public interface LifeCycleListener extends EventListener {

  /**
   * An object is created.
   *
   * @param o the created object
   */
  void created(Object o);

  /**
   * An object is loaded from the session.
   *
   * @param id the id of the object
   * @param path the path of the object
   * @param name the name of the object
   * @param o the object
   */
  void loaded(String id, String path, String name, Object o);

  /**
   * An object is added to the session.
   *
   * @param id the id of the object
   * @param path the path of the object
   * @param name the name of the object
   * @param o the object
   */
  void added(String id, String path, String name, Object o);

  /**
   * An object is removed from the session.
   *
   * @param id the id of the object
   * @param path the path of the object
   * @param name the name of the object
   * @param o the object
   */
  void removed(String id ,String path, String name, Object o);

}

