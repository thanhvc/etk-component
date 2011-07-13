package org.etk.orm.plugins.query;

import org.etk.orm.api.ORMException;

/**
 * A base interface for all queries.
 *
 */
public interface Query<O> {

  /**
   * Executes the query and return the result as a serie of Chromattic entities.
   *
   * @return the query result
   * @throws ChromatticException any chromattic exception
   */
  QueryResult<O> objects() throws ORMException;

  /**
   * Executes the query and return the result as a serie of Chromattic entities with the specified
   * limit and offset.
   *
   * @param offset the optional offset
   * @param limit the optional limit
   * @return the query result
   * @throws ChromatticException any chromattic exception
   * @throws IllegalArgumentException if the offset or limit argument is negative
   */
  QueryResult<O> objects(Long offset, Long limit) throws ORMException;
}
