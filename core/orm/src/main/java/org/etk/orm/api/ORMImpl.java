package org.etk.orm.api;

import javax.jcr.Credentials;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.etk.orm.core.Domain;
import org.etk.orm.core.DomainSessionImpl;
import org.etk.orm.plugins.jcr.SessionLifeCycle;
import org.etk.orm.plugins.jcr.SessionWrapper;
import org.etk.orm.plugins.jcr.SessionWrapperImpl;


public class ORMImpl implements ORM {
  /** . */
  private SessionLifeCycle sessionLifeCycle;

  /** . */
  private Domain domain;

  ORMImpl(Domain domain, SessionLifeCycle sessionLifeCycle) {
    this.domain = domain;
    this.sessionLifeCycle = sessionLifeCycle;
  }

  public SessionLifeCycle getSessionLifeCycle() {
    return sessionLifeCycle;
  }

  public ORMSession openSession() {
    try {
      Session session = sessionLifeCycle.login();
      return build(session);
    }
    catch (RepositoryException e) {
      throw new UndeclaredRepositoryException(e);
    }
  }

  private ORMSession build(Session session) {
    SessionWrapper wrapper = new SessionWrapperImpl(sessionLifeCycle, session, domain.isHasPropertyOptimized(), domain.isHasNodeOptimized());
    return new ORMSessionImpl(new DomainSessionImpl(domain, wrapper));
  }

  public ORMSession openSession(String workspace) {
    try {
      Session session = sessionLifeCycle.login(workspace);
      return build(session);
    }
    catch (RepositoryException e) {
      throw new UndeclaredRepositoryException(e);
    }
  }

  public ORMSession openSession(Credentials credentials, String workspace) {
    try {
      Session session = sessionLifeCycle.login(credentials, workspace);
      return build(session);
    }
    catch (RepositoryException e) {
      throw new UndeclaredRepositoryException(e);
    }
  }

  public ORMSession openSession(Credentials credentials) {
    try {
      Session session = sessionLifeCycle.login(credentials);
      return build(session);
    }
    catch (RepositoryException e) {
      throw new UndeclaredRepositoryException(e);
    }
  }
}