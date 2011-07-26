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
package org.etk.storage.plugins.cache.model.key;

import org.etk.service.foo.model.Foo;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 22, 2011  
 */
public class FooKey implements CacheKey {

  private final String id;
  
  public FooKey(final Foo foo) {
    this.id = foo.getId();
  }

  public String getId() {
    return id;
  }
  
  @Override
  public boolean equals(final Object o) {
    if (this == o) {
     return true; 
    }
    
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    
    FooKey that = (FooKey) o;
    //it's very important to compare when get from Map caching.
    if (id != null? !id.equals(that.id) : that.id != null) {
      return false;
    }
    return true;
  }
  
  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }
}
