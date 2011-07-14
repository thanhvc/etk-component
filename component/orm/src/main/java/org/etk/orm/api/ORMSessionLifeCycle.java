package org.etk.orm.api;

import javax.jcr.Credentials;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.etk.orm.plugins.jcr.SessionLifeCycle;



public class ORMSessionLifeCycle implements SessionLifeCycle {

  /** . */
  private static final Repository repo;

  static {
    try {
      RepositoryBootstrap bootstrap = new RepositoryBootstrap();
      bootstrap.bootstrap();
      repo = bootstrap.getRepository();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public ORMSessionLifeCycle() throws Exception {
  }

  public Session login() throws RepositoryException {
    return repo.login(new SimpleCredentials("exo", "exo".toCharArray()));
  }

  public Session login(String workspace) throws RepositoryException {
    return repo.login(new SimpleCredentials("exo", "exo".toCharArray()), workspace);
  }

  public Session login(Credentials credentials, String workspace) throws RepositoryException {
    return repo.login(credentials, workspace);
  }

  public Session login(Credentials credentials) throws RepositoryException {
    return repo.login(credentials);
  }

  public void save(Session session) throws RepositoryException {
    session.save();
  }

  public void close(Session session) {
    session.logout();
  }
}

