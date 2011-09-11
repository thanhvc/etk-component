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
package org.etk.sandbox.orm.binder;

import java.util.Map;

import org.etk.reflect.api.ClassTypeInfo;
import org.etk.sandbox.orm.binding.BindingVisitor;
import org.etk.sandbox.orm.binding.ETKBinding;
import org.etk.sandbox.orm.binding.MethodBinding;
import org.etk.sandbox.orm.binding.PropertyBinding;
import org.etk.sandbox.orm.core.ObjectContext;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          thanhvucong.78@exoplatform.com
 * Aug 17, 2011  
 */
public class ETKBinderBuilder {

  private final Map<ClassTypeInfo, ETKBinding> bindingMap;
  
  public ETKBinderBuilder(Map<ClassTypeInfo, ETKBinding> bindingMap) {
    this.bindingMap = bindingMap;
  }
  
  public void build() {
    Context ctx = new Context();
    ctx.start();
    
    for (ETKBinding binding : bindingMap.values()){
      binding.accept(ctx);
    }
  }
  
  
  private class Context extends BindingVisitor {

    private ETKBinding binding;
    private Map<ETKBinding, ObjectBinder<?>> bindingBinders;
    private Class<? extends ObjectContext> contextType;
    
    public void start() {
            
    }
    @Override
    public PropertyBinder<?, ?> propertyBinder(PropertyBinding<?> proBinding) {
      return null;
    }
    
    @Override
    public MethodBinder<?> methodBinder(MethodBinding mBinding) {
      return null;
    }
    
    
  }
  
  
}
