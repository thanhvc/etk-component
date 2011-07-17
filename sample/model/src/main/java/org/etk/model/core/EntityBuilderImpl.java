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

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.etk.model.api.EntityBuilder;
import org.etk.model.plugins.entity.binder.BinderBuilder;
import org.etk.model.plugins.entity.binder.ObjectBinder;
import org.etk.model.plugins.entity.binding.EntityBinding;
import org.etk.model.plugins.entity.binding.EntityBindingBuilder;
import org.etk.model.plugins.entity.type.SimpleTypeResolver;
import org.etk.model.plugins.instrument.Instrumentor;
import org.etk.orm.api.BuilderException;
import org.etk.orm.plugins.common.ObjectInstantiator;
import org.etk.reflect.api.ClassTypeInfo;
import org.etk.reflect.api.TypeResolver;
import org.etk.reflect.core.TypeResolverImpl;
import org.etk.reflect.jlr.metadata.JLReflectionMetadata;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 14, 2011  
 */
public class EntityBuilderImpl extends EntityBuilder {

  /** The mappers. */
  private Collection<ObjectBinder<?>> binders;
  
  private Set<ClassTypeInfo> classTypes;
  
  private Collection<EntityBinding> bindings;
  
  @Override
  protected void init(Set<Class<?>> classes) throws BuilderException {
    SimpleTypeResolver propertyTypeResolver = new SimpleTypeResolver();
    TypeResolver<Type> typeResolver = TypeResolverImpl.create(JLReflectionMetadata.newInstance());
    
    //Build mappings
    classTypes = new HashSet<ClassTypeInfo>();
    //converts the classes to the set of ClassTypeInfo via the TypeResolver.
    for (Class clazz : classes) {
      ClassTypeInfo typeInfo = (ClassTypeInfo) typeResolver.resolve(clazz);
      classTypes.add(typeInfo);
      
    }
    
    Map<ClassTypeInfo, EntityBinding> entityBindings = new EntityBindingBuilder().build(classTypes);
    
    Collection<EntityBinding> mappings = entityBindings.values();

    bindings = entityBindings.values();
    // Build binders
    BinderBuilder builder = new BinderBuilder(propertyTypeResolver);
    Collection<ObjectBinder<?>> binders = builder.build(mappings);

    //
    this.binders = binders;
  }
  
  public static final String  INSTRUMENTOR_CLASSNAME ="org.etk.model.apt.InstrumentorImpl";
  
  private <T> T create(String className, Class<T> expectedClass) {
    
    return ObjectInstantiator.newInstance(className, expectedClass);
  }

  @Override
  protected EntitySession boot() throws BuilderException {
    Instrumentor instrumentor = create(INSTRUMENTOR_CLASSNAME, Instrumentor.class);
        
    Entity domain = new Entity(binders, instrumentor);
    
    return new EntitySessionImpl(domain);
  }

  @Override
  public Set<ClassTypeInfo> getClassInfoTypes() {
    return classTypes;
  }

  @Override
  public Collection<EntityBinding> getBindings() {
    return bindings;
  }

  @Override
  public Collection<ObjectBinder<?>> getBinders() {
    return binders;
  }
}
