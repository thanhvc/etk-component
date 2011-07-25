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
package org.etk.kernel.cache.core;

import java.io.Serializable;

import org.etk.kernel.cache.plugins.concurrent.ConcurrentFIFOExoCache;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 25, 2011  
 */
public class SimpleExoCache<K extends Serializable, V> extends ConcurrentFIFOExoCache<K, V> {
  public SimpleExoCache(int maxSize) {
    super(maxSize);
  }

  public SimpleExoCache() {
    super();
  }

  public SimpleExoCache(String name, int maxSize) {
    super(name, maxSize);
  }
}
