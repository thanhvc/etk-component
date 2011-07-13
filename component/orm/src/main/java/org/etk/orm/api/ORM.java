package org.etk.orm.api;

import javax.jcr.Credentials;


public interface ORM {

  /**
   * Opens a session and returns it.
   *
   * @return the session
   * @throws ChromatticException any Chromattic exception
   */
  ORMSession openSession() throws ORMException;

  /**
   * Opens a session for the specified workspace and returns it.
   *
   * @param workspace the workspace name  
   * @return the session
   * @throws ChromatticException any Chromattic exception
   */
  ORMSession openSession(String workspace) throws ORMException;

  /**
   * Opens with the specified credentials  a session for the specified workspace and returns it.
   *
   * @param credentials the credentials
   * @param workspace the workspace name
   * @return the session
   * @throws ChromatticException any Chromattic exception
   */
  ORMSession openSession(Credentials credentials, String workspace) throws ORMException;

  /**
   * Opens with the specified credentials a session and returns it.
   *
   * @param credentials the credentials
   * @return the session
   * @throws ChromatticException any Chromattic exception
   */
  ORMSession openSession(Credentials credentials) throws ORMException;
}
