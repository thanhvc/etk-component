package org.etk.orm.plugins.query;

import javax.jcr.RepositoryException;
import javax.jcr.query.QueryResult;

import org.etk.orm.api.ORMException;
import org.etk.orm.api.UndeclaredRepositoryException;
import org.etk.orm.core.DomainSession;
import org.etk.orm.plugins.jcr.SessionWrapper;


public class QueryImpl<O> implements Query<O> {

  /** . */
  private final javax.jcr.query.Query jcrQuery;

  /** . */
  private final Class<O> clazz;

  /** . */
  private final DomainSession session;

  QueryImpl(DomainSession session, Class<O> clazz, javax.jcr.query.Query jcrQuery) throws RepositoryException {
    this.session = session;
    this.clazz = clazz;
    this.jcrQuery = jcrQuery;
  }

  public org.etk.orm.plugins.query.QueryResult<O> objects() throws ORMException {
    return objects(null, null);
  }

  public org.etk.orm.plugins.query.QueryResult<O> objects(Long offset, Long limit) throws ORMException {
    if (offset != null && offset < 0)
    {
      throw new IllegalArgumentException();
    }
    if (offset != null && offset < 0)
    {
      throw new IllegalArgumentException();
    }

    //
    try {
      SessionWrapper wrapper = session.getSessionWrapper();
      QueryResult result = wrapper.executeQuery(jcrQuery, offset, limit);
      int hits = wrapper.hits(result);
      return new QueryResultImpl<O>(session, result.getNodes(), hits, clazz);
    }
    catch (RepositoryException e) {
      throw new UndeclaredRepositoryException(e);
    }
  }
}
