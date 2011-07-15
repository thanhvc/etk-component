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

import javax.jcr.RepositoryException;

import org.etk.orm.api.UndeclaredRepositoryException;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 15, 2011  
 */
public abstract class EntitySession {
  /** . */
  final Entity entity;

  public EntitySession(Entity entity) {
    this.entity = entity;
  }

  protected abstract ObjectContext _create(Class<?> clazz, String localName) throws NullPointerException, IllegalArgumentException, RepositoryException;
  
  public ObjectContext create(Class<?> clazz, String localName) throws NullPointerException, IllegalArgumentException, UndeclaredRepositoryException {
    try {
      return _create(clazz, localName);
    }
    catch (RepositoryException e) {
      throw new UndeclaredRepositoryException(e);
    }
  }
  
  protected abstract void _setLocalName(EntityContext ctx, String localName) throws RepositoryException;
  
  public final void setLocalName(EntityContext ctx, String localName) throws UndeclaredRepositoryException {
    try {
      _setLocalName(ctx, localName);
    }
    catch (RepositoryException e) {
      throw new UndeclaredRepositoryException(e);
    }
  }
  
  protected abstract String _getLocalName(EntityContext ctx) throws RepositoryException;
  
  public String getLocalName(EntityContext ctx) throws UndeclaredRepositoryException {
    try {
      return _getLocalName(ctx);
    }
    catch (RepositoryException e) {
      throw new UndeclaredRepositoryException(e);
    }
  }
  
  protected abstract EntityContext _getParent(EntityContext ctx) throws RepositoryException;
  public final EntityContext getParent(EntityContext ctx) throws UndeclaredRepositoryException {
    try {
      return _getParent(ctx);
    }
    catch (RepositoryException e) {
      throw new UndeclaredRepositoryException(e);
    }
  }
  
  protected abstract void _remove(EntityContext context) throws RepositoryException;
  
  public final void remove(EntityContext context) throws UndeclaredRepositoryException {
    try {
      _remove(context);
    }
    catch (RepositoryException e) {
      throw new UndeclaredRepositoryException(e);
    }
  }
}
