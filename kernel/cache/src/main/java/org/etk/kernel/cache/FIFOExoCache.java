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
package org.etk.kernel.cache;

import org.etk.kernel.cache.concurrent.ConcurrentFIFOExoCache;

import java.io.Serializable;

/**
 * Created by The eXo Platform SAS Author : Tuan Nguyen
 * tuan08@users.sourceforge.net Sat, Sep 13, 2003 @ Time: 1:12:22 PM
 *
 * @deprecated use {@link ConcurrentFIFOExoCache} instead
 */
@Deprecated
public class FIFOExoCache<K extends Serializable, V> extends ConcurrentFIFOExoCache<K, V> {
  public FIFOExoCache() {
  }

  public FIFOExoCache(int maxSize) {
    super(maxSize);
  }

  public FIFOExoCache(String name, int maxSize) {
    super(name, maxSize);
  }

}
