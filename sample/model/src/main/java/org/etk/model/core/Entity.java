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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.etk.model.core.entity.EntityType;
import org.etk.model.plugins.entity.binder.ObjectBinder;
import org.etk.model.plugins.entity.binding.EntityBinding;
import org.etk.model.plugins.instrument.Instrumentor;
import org.etk.model.plugins.instrument.MethodHandler;
import org.etk.model.plugins.instrument.ProxyType;
import org.etk.model.plugins.json.type.TypeManager;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 15, 2011  
 */
public class Entity {

  final TypeManager typeManager;
  
  /** . */
  private static final ProxyType<?> NULL_PROXY_TYPE = new ProxyType<Object>() {
    public Object createProxy(MethodHandler handler) {
      throw new UnsupportedOperationException("Cannot create proxy for " + handler);
    }

    public Class<?> getType() {
      throw new UnsupportedOperationException("Cannot get proxy type for NULL_PROXY_TYPE");
    }
  };

  /** . */
  private static final Instrumentor NULL_INSTRUMENTOR = new Instrumentor() {

    // This is OK as the class is *stateless*
    @SuppressWarnings("unchecked")
    public <O> ProxyType<O> getProxyClass(Class<O> clazz) {
      return (ProxyType<O>) NULL_PROXY_TYPE;
    }

    public MethodHandler getInvoker(Object proxy) {
      throw new UnsupportedOperationException();
    }
  };


   /** . */
  private final Map<String, ObjectBinder> typeMapperByNodeType;

  /** . */
  private final Map<Class<?>, ObjectBinder> typeMapperByClass;

  /** The default instrumentor */
  private final Instrumentor  defaultInstrumentor;

  /** . */
  private final Map<Class<?>, Instrumentor> proxyTypeToInstrumentor;

  /** . */
  private final Map<Class<?>, Instrumentor> ormTypeToInstrumentor;


  

  public Entity(Collection<ObjectBinder<?>> mappers,
                Instrumentor defaultInstrumentor) {
       
    //
    Map<Class<?>, Instrumentor> proxyTypeToInstrumentor = new HashMap<Class<?>, Instrumentor>();
    Map<Class<?>, Instrumentor> ormTypeToInstrumentor = new HashMap<Class<?>, Instrumentor>();
    
    for (ObjectBinder<?> mapper : mappers) {
      EntityBinding beanMapping = mapper.getBinding();
      Class<?> clazz = (Class<?>) beanMapping.getEntity().getClassType().unwrap();
 
      if (Object.class.equals(clazz)) {
        proxyTypeToInstrumentor.put(clazz, defaultInstrumentor);
        ormTypeToInstrumentor.put(clazz, defaultInstrumentor);
      } else {
        //wrapper the clazz in the ProxyTypeImpl which supports 
        //to get the MethodHandler to invoke the method in clazz.
        proxyTypeToInstrumentor.put(defaultInstrumentor.getProxyClass(clazz).getType(), defaultInstrumentor);
        ormTypeToInstrumentor.put(clazz, defaultInstrumentor);
      }
    }

    //
    Map<String, ObjectBinder> typeMapperByNodeType = new HashMap<String, ObjectBinder>();
    Map<Class<?>, ObjectBinder> typeMapperByClass = new HashMap<Class<?>, ObjectBinder>();
    
    for (ObjectBinder typeMapper : mappers) {
      if (typeMapperByNodeType.containsKey(typeMapper.getEntityTypeName())) {
        throw new IllegalStateException("Duplicate node type name " + typeMapper);
      }
      typeMapperByNodeType.put(typeMapper.getEntityTypeName(), typeMapper);
      typeMapperByClass.put(typeMapper.getObjectClass(), typeMapper);
    }

    

    //
    this.typeMapperByClass = typeMapperByClass;
    this.typeMapperByNodeType = typeMapperByNodeType;
    this.defaultInstrumentor = defaultInstrumentor;
    this.proxyTypeToInstrumentor = proxyTypeToInstrumentor;
    this.ormTypeToInstrumentor = ormTypeToInstrumentor;
    //this class is very important to manage for binding with Json object, ResultSet(JDBC), JCR or any type.
    this.typeManager = new TypeManager();
  }

 

  public MethodHandler getHandler(Object o) {
    Instrumentor instrumentor = proxyTypeToInstrumentor.get(o.getClass());
    return instrumentor != null ? instrumentor.getInvoker(o) : null;
  }

  public <O> ProxyType<O> getProxyType(Class<O> type) {
    Instrumentor instrumentor = ormTypeToInstrumentor.get(type);
    return instrumentor.getProxyClass(type);
  }

  public ObjectBinder getTypeMapper(String nodeTypeName) {
    return typeMapperByNodeType.get(nodeTypeName);
  }

  public ObjectBinder getTypeMapper(Class<?> clazz) {
    return typeMapperByClass.get(clazz);
  }
  
  public EntityType getEntityType(EntityBinding binding, EntityType.Kind kind) {
    
    return new EntityType(binding, kind);
  }
  
}
