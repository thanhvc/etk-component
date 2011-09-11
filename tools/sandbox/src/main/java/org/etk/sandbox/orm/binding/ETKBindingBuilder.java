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
package org.etk.sandbox.orm.binding;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.etk.reflect.api.ClassTypeInfo;
import org.etk.sandbox.orm.info.ETKInfo;
import org.etk.sandbox.orm.info.ETKResolver;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Aug 17, 2011  
 */
public class ETKBindingBuilder {

  Map<ETKInfo, ETKBinding> bindingMap;
  public ETKBindingBuilder() {
    bindingMap = new HashMap<ETKInfo, ETKBinding>();
  }
  
  public Map<ClassTypeInfo, ETKBinding> build(Map<ClassTypeInfo, ETKInfo> entityMap) {

    Context ctx = new Context(new HashSet<ETKInfo>(entityMap.values()));
    bindingMap = ctx.build();
    Map<ClassTypeInfo, ETKBinding> classTypeBindings = new HashMap<ClassTypeInfo, ETKBinding>();
    
    for (Map.Entry<ETKInfo, ETKBinding> binding : bindingMap.entrySet()) {
      classTypeBindings.put(binding.getKey().getClassTypeInfo(), binding.getValue());
    }

    //
    return classTypeBindings;
  }
  
  private class Context {
    final Map<ETKInfo, ETKBinding> etkBindings;
    final Set<ETKInfo> entityList;
    public Context(Set<ETKInfo> entityList) {
      etkBindings = new HashMap<ETKInfo, ETKBinding>();
      this.entityList = entityList;
    }
    public Map<ETKInfo, ETKBinding> build() {
      for(ETKInfo info : entityList) {
        resolve(info);
      }
      return etkBindings;
    }
    private void resolve(ETKInfo info) {
      ETKBinding binding = etkBindings.get(info);
      if (binding == null) {
        etkBindings.put(info, new ETKBinding(info, ""));
      }
    }
    
    
    
  }
}
