package org.etk.core.rest;

/**
 * Object scope identifier.
 */
public enum ComponentLifecycleScope {
  /**
   * New instance of object created foe each request.
   */
  PER_REQUEST,
  /**
   * Singleton lifecycle.
   */
  SINGLETON,
  /**
   * Inversion-of-control framework controls component's lifecycle.
   */
  CONTAINER
}
