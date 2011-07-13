package org.etk.orm.plugins.query;

import javax.jcr.RepositoryException;

import org.etk.orm.api.ORMException;
import org.etk.orm.api.UndeclaredRepositoryException;
import org.etk.orm.core.DomainSession;

public class QueryManager {

  /** . */
  private final String rootNodePath;

  public QueryManager(String rootNodePath) {
    this.rootNodePath = rootNodePath;
  }

  public <O> QueryBuilder<O> createQueryBuilder(DomainSession session, Class<O> fromClass) throws ORMException {
    return new QueryBuilderImpl<O>(session, fromClass, rootNodePath);
  }

  /**
   * Create a query.
   *
   * @param session the current session
   * @param objectClass the expected object class
   * @param statement the query statement
   * @param <O> the object generic type
   * @return the query
   */
  public <O> Query<O> getObjectQuery(DomainSession session, Class<O> objectClass, String statement) {
    try {
      // For now we support on SQL
      javax.jcr.query.Query jcrQuery = session.getSessionWrapper().createQuery(statement);
      Query<?> query = new QueryImpl<O>(session, objectClass, jcrQuery);

      //
      @SuppressWarnings("unchecked") Query<O> ret = (Query<O>)query;
      return ret;
    }
    catch (RepositoryException e) {
      throw new UndeclaredRepositoryException(e);
    }
  }
}
