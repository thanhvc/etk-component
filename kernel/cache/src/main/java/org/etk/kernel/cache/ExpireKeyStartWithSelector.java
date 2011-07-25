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

import java.io.Serializable;

/**
 * Created by The eXo Platform SAS Author : Thuannd nhudinhthuan@yahoo.com Apr
 * 4, 2006
 */
public class ExpireKeyStartWithSelector<K extends Serializable, V> implements CachedObjectSelector<K, V> {

  private String keyStartWith_;

  public ExpireKeyStartWithSelector(String keyStartWith) {
    keyStartWith_ = keyStartWith;
  }

  public boolean select(K key, ObjectCacheInfo<? extends V> ocinfo) {
    String skey = (String) key;
    if (skey.startsWith(keyStartWith_))
      return true;
    return false;
  }

  public void onSelect(ExoCache<? extends K, ? extends V> cache, K key, ObjectCacheInfo<? extends V> ocinfo) {
    cache.remove(key);
  }
}
