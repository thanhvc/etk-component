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
package org.etk.sandbox.orm.builder;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.etk.reflect.api.ClassTypeInfo;
import org.etk.reflect.api.TypeResolver;
import org.etk.reflect.core.TypeResolverImpl;
import org.etk.reflect.jlr.metadata.JLReflectionMetadata;
import org.etk.sandbox.orm.binding.ETKBinding;
import org.etk.sandbox.orm.core.ETKSession;
import org.etk.sandbox.orm.core.ETKSessionImpl;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Aug 17, 2011  
 */
public abstract class ETKBuilder {

  protected Set<Class<?>> classes = null;
  protected Map<String, ClassTypeInfo> classTypes = null;
  protected Map<ClassTypeInfo, ETKBinding> bindings = null;
  /** Used for receiving {@code java.lang.Object} */
  private final TypeResolver<Type> domain = TypeResolverImpl.create(JLReflectionMetadata.newInstance());
  
  public ETKBuilder() {
    classes = new HashSet<Class<?>>();
    
  }
  
  public void add(Class<?> clazz) {
    classes.add(clazz);
    
  }
  
  /**
   * Executes the binding object.
   */
  public void build() {
    init();
    binding();
    binder();
  }
  
  abstract void binding();
  abstract void binder();
  
 
  /**
   * Executes the initialize
   */
  private void init() {
    //SimpleTypeResolver simple = new SimpleTypeResolver();
    TypeResolver resolver = TypeResolverImpl.create(JLReflectionMetadata.newInstance());
    classTypes = new HashMap<String, ClassTypeInfo>();
    for(Class<?> clazz : classes) {
      ClassTypeInfo classTypeInfo = (ClassTypeInfo) resolver.resolve(clazz);
      classTypes.put(clazz.getName(), classTypeInfo);
    }
  }
  
  
  /**
   * Creates the ETKSession for entity.
   * @return
   */
  public ETKSession boot() {
    
    return new ETKSessionImpl();
  }
  
  
  public Set<Class<?>> getClasses() {
    return classes;
  }
 
  public Map<String, ClassTypeInfo> getClassTypes() {
    return classTypes;
  }
}
