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
package org.etk.sandbox.orm.plugins.json.binder;


import org.etk.sandbox.orm.binder.PropertyBinder;
import org.etk.sandbox.orm.binding.PropertyBinding;
import org.etk.sandbox.orm.core.ObjectContext;
import org.etk.sandbox.orm.info.PropertyInfo;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          thanhvucong.78@google.com
 * Aug 19, 2011  
 */
public class JsonProperty<O extends ObjectContext<O>> extends PropertyBinder<PropertyInfo, O> {

  private final String jsonPropertyName;
  
  public JsonProperty(Class<O> contextType, PropertyBinding pBinding) {
    super(contextType, pBinding);
    this.jsonPropertyName = pBinding.getName();
  }
  
  @Override
  public Object get(O context) throws Throwable {
    return get(context);
  }

 
}
