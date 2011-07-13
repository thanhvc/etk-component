package org.etk.orm.core;

import org.etk.orm.api.Status;
import org.etk.orm.plugins.instrument.ProxyType;
import org.etk.orm.plugins.jcr.NodeTypeInfo;
import org.etk.orm.plugins.mapper.ObjectMapper;


public final class EmbeddedContext extends ObjectContext<EmbeddedContext> {

  /** The object instance. */
  final Object object;

  /** The related type. */
  final ObjectMapper<EmbeddedContext> mapper;

  /** The related entity if not null, otherwise it means that we are not attached to anything. */
  EntityContext relatedEntity;

  /** The related type info. */
  NodeTypeInfo typeInfo;

  /** . */
  final DomainSession session;

  EmbeddedContext(ObjectMapper<EmbeddedContext> mapper, DomainSession session) {

    // Create our proxy
    ProxyType pt = session.domain.getProxyType(mapper.getObjectClass());
    Object object = pt.createProxy(this);

    //
    this.mapper = mapper;
    this.object = object;
    this.session = session;
  }

  /**
   * Returns the status of the related entity when the context is attached to an entity otherwise it returns
   * the {@link Status#TRANSIENT} value.
   *
   * @return the status
   */
  @Override
  public Status getStatus() {
    if (relatedEntity == null) {
      return Status.TRANSIENT;
    } else {
      return relatedEntity.getStatus();
    }
  }

  @Override
  public ObjectMapper<EmbeddedContext> getMapper() {
    return mapper;
  }

  public DomainSession getSession() {
    return session;
  }

  @Override
  public NodeTypeInfo getTypeInfo() {
    if (typeInfo == null) {
      throw new IllegalStateException();
    }
    return typeInfo;
  }

  @Override
  public Object getObject() {
    return object;
  }

  @Override
  public EntityContext getEntity() {
    return relatedEntity;
  }

  @Override
  public String toString() {
    return "EmbeddedContext[mapper=" + mapper + ",related=" + relatedEntity + "]";
  }
}
