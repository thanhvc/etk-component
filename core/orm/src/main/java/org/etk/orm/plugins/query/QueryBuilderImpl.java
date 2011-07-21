package org.etk.orm.plugins.query;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.etk.orm.core.Domain;
import org.etk.orm.core.DomainSession;
import org.etk.orm.plugins.bean.mapping.NodeTypeKind;
import org.etk.orm.plugins.mapper.ObjectMapper;

public class QueryBuilderImpl<O> implements QueryBuilder<O> {

  /** . */
  private final String rootNodePath;

  /** . */
  private Class<O> fromClass;

  /** . */
  private String where;

  /** . */
  private ObjectMapper mapper;

  /** . */
  private DomainSession session;

  QueryBuilderImpl(DomainSession session, Class<O> fromClass, String rootNodePath) throws NullPointerException, IllegalArgumentException {
    if (session == null) {
      throw new NullPointerException("No null domain session accepted");
    }
    if (fromClass == null) {
      throw new NullPointerException("No null from class accepted");
    }
    if (rootNodePath == null) {
      throw new NullPointerException("No null root node path accepted");
    }

    //
    Domain domain = session.getDomain();
    ObjectMapper mapper = domain.getTypeMapper(fromClass);
    if (mapper == null) {
      throw new IllegalArgumentException("Class " + fromClass.getName() + " is not mapped");
    }
    if (mapper.getKind() != NodeTypeKind.PRIMARY) {
      throw new IllegalArgumentException("Class " + fromClass.getName() + " is mapped to a mixin type");
    }

    //
    this.fromClass = fromClass;
    this.mapper = mapper;
    this.where = null;
    this.session = session;
    this.rootNodePath = rootNodePath;
  }

  public QueryBuilder<O> where(String whereStatement) {
    if (whereStatement == null) {
      throw new NullPointerException();
    }
    this.where = whereStatement;
    return this;
  }

  public QueryBuilder<O> orderBy(String orderBy) throws NullPointerException {
    throw new UnsupportedOperationException("todo");
  }

  /** This is not the way I like to do things, but well for now it'll be fine. */
  private static final Pattern JCR_LIKE_PATH = Pattern.compile("jcr:path[\\s]*[^\\s]+[\\s]*'[^']*'");

  public Query<O> get() {
    if (fromClass == null) {
      throw new IllegalStateException();
    }

    //
    StringBuilder sb = new StringBuilder("SELECT * FROM ");

    //
    sb.append(mapper.getNodeTypeName());

    //
    if (where != null) {
      Matcher matcher = JCR_LIKE_PATH.matcher(where);
      if (!matcher.find()) {
        sb.append(" WHERE jcr:path LIKE '").append(rootNodePath).append("/%'");
        sb.append(" AND ");
        sb.append(where);
      } else {
        sb.append(" WHERE ");
        sb.append(where);
      }
    } else {
      sb.append(" WHERE jcr:path LIKE '").append(rootNodePath).append("/%'");
    }

    //
    return session.getDomain().getQueryManager().getObjectQuery(session, fromClass, sb.toString());
  }
}
