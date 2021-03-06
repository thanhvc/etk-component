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
package org.etk.service.foo.api;

import java.util.List;

import org.etk.common.utils.LazyListAccess;
import org.etk.common.utils.ListAccessValidator;
import org.etk.service.foo.model.Foo;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 21, 2011  
 */
public class FooListAccess implements LazyListAccess<Foo> {

  /** The list used for identity storage. */
  private final List<Foo> list;
  
  public FooListAccess(List<Foo> list) {
    this.list = list;
  }
  
  @Override
  public Foo[] load(int offset, int limit) throws Exception, IllegalArgumentException {
    ListAccessValidator.validateIndex(offset, limit, this.getSize());
    Foo result[] = new Foo[limit];
    for (int i = 0; i < limit; i++)
      result[i] = list.get(i + offset);

    return result;
  }

  @Override
  public int getSize() throws Exception { return list.size();}
}
