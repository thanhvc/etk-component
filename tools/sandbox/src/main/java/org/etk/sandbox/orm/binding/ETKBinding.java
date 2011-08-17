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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.etk.sandbox.orm.info.ETKInfo;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Aug 17, 2011  
 */
public class ETKBinding {

  private ETKBinding parent;
  private final ETKInfo etkInfo;
  
  final Map<String, PropertyBinding> properties;
  final Map<String, PropertyBinding> unmodifiableProperties;
  
  final List<MethodBinding> methods;
  final List<MethodBinding> unmodifiableMethods;
  public ETKBinding(ETKInfo etkInfo) {
    this.etkInfo = etkInfo;
    
    this.properties = new HashMap<String, PropertyBinding>();
    this.unmodifiableProperties = Collections.unmodifiableMap(this.properties);
    
    this.methods = new ArrayList<MethodBinding>();
    this.unmodifiableMethods = Collections.unmodifiableList(this.methods);

  }
  
  public void accept(BindingVisitor visitor) {
    visitor.startBean(this);
    for (PropertyBinding property : properties.values()) {
      property.accept(visitor);
    }
    for (MethodBinding method : methods) {
      method.accept(visitor);
    }
    visitor.endBean();
  }
}
