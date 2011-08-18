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
package org.etk.sandbox.orm.info;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;



import org.etk.reflect.api.ClassTypeInfo;
import org.etk.reflect.api.MethodInfo;
import org.etk.reflect.api.SimpleTypeInfo;
import org.etk.reflect.api.TypeInfo;
import org.etk.reflect.api.VoidTypeInfo;
import org.etk.reflect.api.definition.ClassKind;
import org.etk.reflect.api.introspection.MethodIntrospector;
import org.etk.reflect.api.visit.HierarchyVisitor;
import org.etk.reflect.api.visit.HierarchyVisitorStrategy;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Aug 17, 2011  
 */
public class ETKResolver {

  public Map<ClassTypeInfo, ETKInfo> build(Map<String, ClassTypeInfo> classTypes) {
 
    Context ctx = new Context(classTypes);
    ctx.build();
    return ctx.etkInfos;
  }

  private class Context {
    final Map<String, ClassTypeInfo> classTypes;
    final Map<ClassTypeInfo, ETKInfo> etkInfos;
    
    private class EntityHierarchyVisitorStrategy<V extends HierarchyVisitor<V>> extends HierarchyVisitorStrategy<V> {

      /** . */
      private final ClassTypeInfo current;

      private EntityHierarchyVisitorStrategy(ClassTypeInfo current) {
        this.current = current;
      }

      @Override
      protected boolean accept(ClassTypeInfo type) {
        return type == current || !classTypes.containsValue(type);
      }
    }
    
    public Context(Map<String, ClassTypeInfo> classTypes) {
      this.classTypes = classTypes;
      this.etkInfos = new HashMap<ClassTypeInfo, ETKInfo>();
    }

    public void build() {
      Iterator<ClassTypeInfo> it = classTypes.values().iterator();
      
      while(it.hasNext()) {
        ClassTypeInfo info = it.next();
        resolve(info);
      }
    }

    private void resolve(ClassTypeInfo info) {
     ETKInfo got = etkInfos.get(info);
     if (got == null) {
       
       boolean accept;
       if (info.getKind() == ClassKind.CLASS || info.getKind() == ClassKind.INTERFACE) {
         //isPrimitive Object LONG, DOUBLE, STRING, DATE ...
         if (info instanceof SimpleTypeInfo) {
           accept = false;
         } else if (info instanceof VoidTypeInfo) {
           accept = false;
         } else {
             accept = true;
         }
       } else {
         accept = false;
       }
       //Builds the ETKInfo
       if(accept == true) {
         ETKInfo etkInfo = new ETKInfo(info);
         etkInfos.put(info, etkInfo);
         build(etkInfo);
       }
     }
      
    }

    private void build(ETKInfo etkInfo) {
     buildProperties(etkInfo);
     buildMethods(etkInfo);
    }

    private void buildMethods(ETKInfo etkInfo) {
      ClassTypeInfo classTypeInfo = etkInfo.classTypeInfo;
      
    }
    
    class ToBuild {
      final TypeInfo type;
      final MethodInfo getter;
      final MethodInfo setter;
      ToBuild(TypeInfo type, MethodInfo getter, MethodInfo setter) {
        this.type = type;
        this.getter = getter;
        this.setter = setter;
      }
    }

    /**
     * Creates the Property for ETKInfo which contains the Setter and Getter
     * @param etkInfo
     */
    private void buildProperties(ETKInfo etkInfo) {
      EntityHierarchyVisitorStrategy strategy = new EntityHierarchyVisitorStrategy(etkInfo.classTypeInfo);
      MethodIntrospector introspector = new MethodIntrospector(strategy, true);
      Map<String, MethodInfo> getterMap = introspector.getGetterMap(etkInfo.classTypeInfo);
      Map<String, Set<MethodInfo>> setterMap = introspector.getSetterMap(etkInfo.classTypeInfo);
      
     // Gather all properties on the bean, to build the setter, getter pair for propertybinding
     Map<String, ToBuild> toBuilds = new HashMap<String,ToBuild>();
     
     for (Map.Entry<String, MethodInfo> entry : getterMap.entrySet()) {
       String name = entry.getKey();
       MethodInfo getter = entry.getValue();
       TypeInfo getterReturnTypeInfo = getter.getReturnType();
       
       ToBuild toBuild = null;
       Set<MethodInfo> setters = setterMap.get(name);
       for (MethodInfo setter : setters) {
         //Check the returnType of Getter equal Parameter[0] of setter 
         TypeInfo setterTypeInfo = setter.getParameterTypes().get(0);
         if (getterReturnTypeInfo.equals(setterTypeInfo)) {
           toBuild = new ToBuild(getterReturnTypeInfo, getter, setter);
           break;
         }
       }
       
       if (toBuild == null) {
         //property read-only(existing the getter)
         toBuild = new ToBuild(getterReturnTypeInfo, getter, null);
       }
       
       if (toBuild != null) {
         toBuilds.put(name, toBuild);
       }
     }
     //Removes all of setter which was existing in the ToBuilds Map.
     setterMap.keySet().removeAll(toBuilds.keySet());
     //Makes the property which only has the setter.
     for (Map.Entry<String, Set<MethodInfo>> setterEntry : setterMap.entrySet()) {
       String name = setterEntry.getKey();
       for (MethodInfo setter : setterEntry.getValue()) {
         TypeInfo setterTypeInfo = setter.getParameterTypes().get(0);
         toBuilds.put(name, new ToBuild(setterTypeInfo, null, setter));
       }
     }
     
     // Now we have all the info to build each property correctly
     Map<String, PropertyInfo> properties = new HashMap<String, PropertyInfo>();     
     //resolve the ValueKind of Property
     for (Map.Entry<String, ToBuild> toBuildEntry : toBuilds.entrySet()) {
             
        PropertyInfo property = new PropertyInfo(etkInfo,
                                                 toBuildEntry.getKey(),
                                                 toBuildEntry.getValue().getter,
                                                 toBuildEntry.getValue().setter);
        properties.put(property.getName(), property);
     }
     
     etkInfo.properties.putAll(properties);
     
    }
  }
}
