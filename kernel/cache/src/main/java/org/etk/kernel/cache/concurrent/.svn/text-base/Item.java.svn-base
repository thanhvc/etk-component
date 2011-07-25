/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.services.cache.concurrent;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class Item {

  private static final AtomicLong generator = new AtomicLong(0);

  final long serial = generator.incrementAndGet();
  final int hashCode = (int)(serial % Integer.MAX_VALUE);
  Item previous;
  Item next;

  /**
   * This is final on purpose, we rely on object equality in the concurrent has
   */
  public final boolean equals(Object obj) {
    return ((Item)obj).serial == serial;
  }

  /**
   * This is final on purpose, we rely on object equality in the concurrent has
   */
  public final int hashCode() {
    return hashCode;
  }
}

