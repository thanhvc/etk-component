package org.etk.orm.plugins.jcr;

import javax.jcr.Session;
import javax.jcr.Credentials;
import javax.jcr.RepositoryException;

public interface SessionLifeCycle {

  Session login() throws RepositoryException;

  Session login(String workspace) throws RepositoryException;

  Session login(Credentials credentials, String workspace) throws RepositoryException;

  Session login(Credentials credentials) throws RepositoryException;

  void save(Session session) throws RepositoryException;

  void close(Session session);
}