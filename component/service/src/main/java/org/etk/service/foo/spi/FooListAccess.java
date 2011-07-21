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
package org.etk.service.foo.spi;

import java.util.List;

import org.etk.common.utils.LazyListAccess;
import org.etk.common.utils.ListAccessValidator;
import org.etk.service.foo.FooFilter;
import org.etk.service.foo.mock.FooStorage;
import org.etk.service.foo.model.Foo;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 21, 2011  
 */
public class FooListAccess implements LazyListAccess<Foo> {

  FooStorage fooStorage;
  FooFilter fooFilter;
  /** The type. */
  Type type;
  
  /**
   * The foo list access Type Enum.
   */
  public enum Type {
    /** Gets the all Foos (for super user). */
    ALL,
    /** Gets the all Foos by filter. */
    ALL_FILTER,
  }
  
  public FooListAccess(FooStorage fooStorage, Type type) {
    this.fooStorage = fooStorage;
    this.type = type;
  }
  
  @Override
  public Foo[] load(int offset, int limit) throws Exception, IllegalArgumentException {
    ListAccessValidator.validateIndex(offset, limit, this.getSize());
    List<Foo> listFoos = null;
    switch (type) {
      case ALL: listFoos = fooStorage.getFoos(offset, limit);
        break;
      case ALL_FILTER: listFoos = fooStorage.getFooByFilter(this.fooFilter, offset, limit);
        break;
    }
    return listFoos.toArray(new Foo[listFoos.size()]);
  }

  @Override
  public int getSize() throws Exception {
    switch (type) {
      case ALL: return 0;
      case ALL_FILTER: return 0;
      default: return 0;
    }
  }
}
