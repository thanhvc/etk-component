package org.etk.orm.plugins.query;

import java.util.Iterator;

/**
 * <p>The result for an query.</p>
 *
 */
public interface QueryResult<R> extends Iterator<R> {

  /**
   * Returns the number of results this query will returns until the iterator has no more elements to return.
   *
   * @return the result size
   */
  int size();

  /**
   * Returns the number of hits this query matched regardless of the limit and offset set on the query builder when
   * the query was built.
   *
   * @return the result hits
   */
  int hits();

}
