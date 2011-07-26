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

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 22, 2011  
 */
public class ListFoosKey extends ListCacheKey {

  private final FooFilterKey key;
  public ListFoosKey(final FooFilterKey key, final long offset, final long limit) {

    super(offset, limit);
    this.key = key;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    
    if (!(o instanceof ListFoosKey)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    
    ListFoosKey that = (ListFoosKey) o;
    if (key != null ? !key.equals(that.key) : that.key != null) {
      return false;
    }
    
    return true;
  }
  
  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (key != null ? key.hashCode() : 0);
    return result;
  }
}
