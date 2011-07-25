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
package org.etk.kernel.cache.concurrent;

import org.etk.kernel.cache.CacheInfo;
import org.etk.kernel.cache.CacheListener;
import org.etk.kernel.cache.CacheListenerContext;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class ListenerContext<K, V> implements CacheListenerContext, CacheInfo {

  /** . */
  private final ConcurrentFIFOExoCache cache;

  /** . */
  final CacheListener<? super K, ? super V> listener;

  public ListenerContext(CacheListener<? super K, ? super V> listener, ConcurrentFIFOExoCache cache) {
    this.listener = listener;
    this.cache = cache;
  }

  public CacheInfo getCacheInfo() {
    return this;
  }

  public String getName() {
    return cache.getName();
  }

  public int getMaxSize() {
    return cache.getMaxSize();
  }

  public long getLiveTime() {
    return cache.getLiveTime();
  }

  public int getSize() {
    return cache.getCacheSize();
  }

  void onExpire(K key, V obj) {
    try {
      listener.onExpire(this, key, obj);
    }
    catch (Exception ignore) {
    }
  }

  void onRemove(K key, V obj) {
    try {
      listener.onRemove(this, key, obj);
    }
    catch (Exception ignore) {
    }
  }

  void onPut(K key, V obj) {
    try {
      listener.onPut(this, key, obj);
    }
    catch (Exception ignore) {
    }
  }

  void onGet(K key, V obj) {
    try {
      listener.onGet(this, key, obj);
    }
    catch (Exception ignore) {
    }
  }

  void onClearCache() {
    try {
      listener.onClearCache(this);
    }
    catch (Exception ignore) {
    }
  }
}
