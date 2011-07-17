/*
 * Copyright (C) 2003-2011 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.etk.model.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.etk.common.logging.Logger;
import org.etk.model.core.entity.EntityType;
import org.etk.model.core.entity.EntityTypeInfo;
import org.etk.model.plugins.entity.binder.ObjectBinder;
import org.etk.model.plugins.entity.binding.AttributeType;
import org.etk.model.plugins.instrument.ProxyType;
import org.etk.orm.api.Status;
import org.etk.orm.api.UndeclaredRepositoryException;
import org.etk.orm.core.NameKind;
import org.etk.orm.plugins.bean.mapping.NodeAttributeType;
import org.etk.orm.plugins.jcr.LinkType;
import org.etk.orm.plugins.mapper.ObjectMapper;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 15, 2011  
 */
public final class EntityContext extends ObjectContext<EntityContext> {

  /** The logger. */
  private static final Logger log = Logger.getLogger(EntityContext.class);

  /** The related type. */
  final ObjectBinder<EntityContext> mapper;

  /** The object instance. */
  final Object object;
  
  private EntityType entityType;

  /** The attributes. */
  private Map<Object, Object> attributes;
  
  EntitySession session;

  EntityContext(EntityType entityType, ObjectBinder<EntityContext> mapper, EntitySession session) throws RepositoryException {

    // Create our proxy
    ProxyType proxyType = session.entity.getProxyType(mapper.getObjectClass());
    Object object = proxyType.createProxy(this);

    //
    this.session = session;
    this.mapper = mapper;
    this.object = object;
    this.attributes = null;
    this.entityType = entityType;
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

  public EntitySession getSession() {
    return getSession();
  }

 
  @Override
  public ObjectBinder<EntityContext> getMapper() {
    return mapper;
  }

  public Object getObject() {
    return object;
  }

  @Override
  public EntityContext getEntity() {
    return this;
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
    } 
    //
    return null;
  }

 
  

  public String getAttribute(AttributeType type) {
    switch (type) {
      case NAME:
        return session.getLocalName(this);
      default:
        throw new AssertionError();
    }
  }

  public void remove() {
    session.remove(this);
  }

 
  public String getLocalName() {
    return getAttribute(AttributeType.NAME);
  }

  
  public void setLocalName(String name) {
    session.setLocalName(this, name);
  }

  
  public EntityContext getParent() {
    return session.getParent(this);
  }

  @Override
  public String toString() {
    return "EntityContext[session=" + session + ",mapper=" + mapper + "]";
  }

  @Override
  public EntityTypeInfo getTypeInfo() {
    //getEntityTypeInfo from
    return session.entity.typeManager.getEntityTypeInfo(entityType);
  }
}