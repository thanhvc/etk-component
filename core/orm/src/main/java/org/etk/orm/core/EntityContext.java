package org.etk.orm.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.etk.common.logging.Logger;
import org.etk.orm.api.Status;
import org.etk.orm.api.UndeclaredRepositoryException;
import org.etk.orm.plugins.bean.mapping.NodeAttributeType;
import org.etk.orm.plugins.instrument.ProxyType;
import org.etk.orm.plugins.jcr.LinkType;
import org.etk.orm.plugins.jcr.type.PrimaryTypeInfo;
import org.etk.orm.plugins.mapper.ObjectMapper;


public final class EntityContext extends ObjectContext<EntityContext> {

  /** The logger. */
  private static final Logger log = Logger.getLogger(EntityContext.class);

  /** The related type. */
  final ObjectMapper<EntityContext> mapper;

  /** The object instance. */
  final Object object;

  /** The related state. */
  EntityContextState state;

  /** The attributes. */
  private Map<Object, Object> attributes;

  EntityContext(ObjectMapper<EntityContext> mapper, EntityContextState state) throws RepositoryException {

    // Create our proxy
    ProxyType proxyType = state.getSession().domain.getProxyType(mapper.getObjectClass());
    Object object = proxyType.createProxy(this);

    //
    this.state = null;
    this.mapper = mapper;
    this.object = object;
    this.state = state;
    this.attributes = null;
  }

  public Object getAttribute(Object key) {
    if (key == null) {
      throw new AssertionError("Should not provide a null key");
    }
    if (attributes == null) {
      return null;
    } else {
      return attributes.get(key);
    }
  }

  public void setAttribute(Object key, Object value) {
    if (key == null) {
      throw new AssertionError("Should not provide a null key");
    }
    if (value == null) {
      if (attributes != null) {
        attributes.remove(key);
      }
    } else {
      if (attributes == null) {
        attributes = new HashMap<Object, Object>();
      }
      attributes.put(key, value);
    }
  }

  public Node getNode() {
    return state.getNode();
  }

  public DomainSession getSession() {
    return state.getSession();
  }

  public Status getStatus() {
    return state.getStatus();
  }

  @Override
  public ObjectMapper<EntityContext> getMapper() {
    return mapper;
  }

  public Object getObject() {
    return object;
  }

  @Override
  public EntityContext getEntity() {
    return this;
  }

  public PrimaryTypeInfo getTypeInfo() {
    EntityContextState state = getEntity().state;
    return state.getTypeInfo();
  }

  public String decodeName(String name, NameKind nameKind) {
    try {
      return state.getSession().getDomain().decodeName(this, name, nameKind);
    }
    catch (RepositoryException e) {
      throw new UndeclaredRepositoryException(e);
    }
  }

  public String encodeName(String name, NameKind nameKind) {
    try {
      return state.getSession().getDomain().encodeName(this, name, nameKind);
    }
    catch (RepositoryException e) {
      throw new UndeclaredRepositoryException(e);
    }
  }

  /**
   * Adapts the current object held by this context to the specified type.
   * If the current object is an instance of the specified class then this
   * object is returned otherwise an attempt to find an embedded object of
   * the specified type is performed.
   *
   * @param adaptedClass the class to adapt to
   * @param <T> the parameter type of the adapted class
   * @return the adapted object or null
   */
  public <T> T adapt(Class<T> adaptedClass) {
    // If it fits the current object we use it
    if (adaptedClass.isInstance(object)) {
      return adaptedClass.cast(object);
    } else {
      // Here we are trying to see if the parent has something embedded we could return
      // that is would be of the provided related class
      EmbeddedContext embeddedCtx = getEmbedded(adaptedClass);
      if (embeddedCtx != null) {
        return adaptedClass.cast(embeddedCtx.getObject());
      }
    }

    //
    return null;
  }

  public void addMixin(EmbeddedContext mixinCtx) {
    state.getSession().addMixin(this, mixinCtx);
  }

  public EmbeddedContext getEmbedded(Class<?> embeddedClass) {
    return state.getSession().getEmbedded(this, embeddedClass);
  }

  public String getAttribute(NodeAttributeType type) {
    DomainSession session = state.getSession();
    switch (type) {
      case NAME:
        return session.getLocalName(this);
      case ID:
        return state.getId();
      case PATH:
        return state.getPath();
      case WORKSPACE_NAME:
        return session.sessionWrapper.getSession().getWorkspace().getName();
      default:
        throw new AssertionError();
    }
  }

  public void remove() {
    state.getSession().remove(this);
  }

  public <T> Iterator<T> getReferents(final String name, Class<T> filterClass, LinkType linkType) {
    return state.getSession().getReferents(this, name, filterClass, linkType);
  }

  public String getLocalName() {
    return getAttribute(NodeAttributeType.NAME);
  }

  public String getId() {
    return getAttribute(NodeAttributeType.ID);
  }

  public String getPath() {
    return getAttribute(NodeAttributeType.PATH);
  }

  public void setLocalName(String name) {
    state.getSession().setLocalName(this, name);
  }

  public EntityContext getReferenced(String name, LinkType linkType) {
    return state.getSession().getReferenced(this, name, linkType);
  }

  public void setReferenced(String name, EntityContext referencedCtx, LinkType linkType) {
    DomainSession session = state.getSession();
    session.setReferenced(this, name, referencedCtx, linkType);
  }

  public boolean addReference(String name, EntityContext referentCtx, LinkType linkType) {
    DomainSession session = state.getSession();
    return session.setReferenced(referentCtx, name, this, linkType);
  }


  public EntityContext getParent() {
    return state.getSession().getParent(this);
  }

  @Override
  public String toString() {
    return "EntityContext[state=" + state + ",mapper=" + mapper + "]";
  }
}
