package org.etk.orm.plugins.query;


/**
 * The query builder allows to create queries.
 *
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public interface QueryBuilder<O> {

  /**
   * <p>Set the where clause of the query.</p>
   *
   * @param where the where clause
   * @return this builder
   * @throws NullPointerException if the argument is null
   */
  QueryBuilder<O> where(String where) throws NullPointerException;

  /**
   * <p>Set the order by clause of the query.</p>
   *
   * @param orderBy the order by clause
   * @return this builder
   * @throws NullPointerException if the argument is null
   */
  QueryBuilder<O> orderBy(String orderBy) throws NullPointerException;

  /**
   * <p>Compute and returns the <tt>ObjectQuery</tt> for this builder.</p>
   *
   * @return this object query
   * @throws IllegalStateException if the builder cannot build the query
   */
  Query<O> get() throws IllegalStateException;

}
