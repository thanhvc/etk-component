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

import java.util.Map;

import org.etk.reflect.api.ClassTypeInfo;
import org.etk.sandbox.orm.binding.ETKBinding;
import org.etk.sandbox.orm.binding.ETKBindingBuilder;
import org.etk.sandbox.orm.info.ETKInfo;
import org.etk.sandbox.orm.info.ETKResolver;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Aug 17, 2011  
 */
public class ETKBuilderImpl extends ETKBuilder {
  
  
  /**
   * Executes the binding
   */
  @Override
  protected void binding() {
    Map<ClassTypeInfo, ETKInfo> etkMap = new ETKResolver().build(classTypes);
    bindings = new ETKBindingBuilder().build(etkMap);
    
  }

  @Override
  protected void binder() {
    
    
  }
}
