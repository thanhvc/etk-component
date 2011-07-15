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
import java.util.Map;

import javax.jcr.RepositoryException;

import org.etk.model.plugins.entity.binder.ObjectBinder;
import org.etk.model.plugins.entity.binding.EntityTypeKind;
import org.etk.orm.core.Domain;
import org.etk.orm.plugins.bean.mapping.NodeTypeKind;
import org.etk.orm.plugins.jcr.SessionWrapper;
import org.etk.orm.plugins.mapper.ObjectMapper;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 15, 2011  
 */
public class EntitySessionImpl extends EntitySession {

  
  /** . */
  final Entity entity;

  /** . */
  private Map<String, EntityContext> contexts;

  public EntitySessionImpl(Entity entity) {
    super(entity);

    //
    this.entity = entity;
    this.contexts = new HashMap<String, EntityContext>();
  }
  
  
  protected ObjectContext _create(Class<?> clazz, String localName) throws NullPointerException,
                                                                   IllegalArgumentException,
                                                                   RepositoryException {
    if (clazz == null) {
      throw new NullPointerException();
    }

    //
    ObjectBinder<?> typeMapper = entity.getTypeMapper(clazz);
    if (typeMapper == null) {
      throw new IllegalArgumentException("The type " + clazz.getName() + " is not mapped");
    }

    //
    if (typeMapper.getKind() == EntityTypeKind.ENTITY && typeMapper.isAbstract()) {
      throw new IllegalArgumentException("The type " + clazz.getName() + " is abstract");
    }

    //

    EntityContext ctx = new EntityContext((ObjectBinder<EntityContext>) typeMapper, this);

    //
    if (localName != null) {
      ctx.setLocalName(localName);
    }

    return ctx;
  }


  @Override
  protected void _setLocalName(EntityContext ctx, String localName) throws RepositoryException {
    // TODO Auto-generated method stub
    
  }


  @Override
  protected String _getLocalName(EntityContext ctx) throws RepositoryException {
    // TODO Auto-generated method stub
    return null;
  }


  @Override
  protected EntityContext _getParent(EntityContext ctx) throws RepositoryException {
    // TODO Auto-generated method stub
    return null;
  }


  @Override
  protected void _remove(EntityContext context) throws RepositoryException {
    // TODO Auto-generated method stub
    
  }
}
