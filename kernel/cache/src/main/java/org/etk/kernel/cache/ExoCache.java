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
import java.util.List;
import java.util.Map;

/**
 * A bare cache.
 *
 * Created by The eXo Platform SAS. Author : Tuan Nguyen
 * tuan08@users.sourceforge.net Date: Jun 14, 2003 Time: 1:12:22 PM
 *
 * @param <K> the type of keys maintained by this cache
 * @param <V> the type of cached values
 */

public interface ExoCache<K extends Serializable, V> {

  /**
   * Returns the cache name
   *
   * @return the cache name
   */
  public String getName();

  /**
   * Sets the cache name.
   *
   * @param name the cache name
   */
  public void setName(String name);

  public String getLabel();

  public void setLabel(String s);

  /**
   * Performs a lookup operation.
   *
   * @param key the cache key
   * @return the cached value which may be evaluated to null
   */
  public V get(Serializable key);

  /**
   * Removes an entry from the cache.
   *
   * @param key the cache key
   * @return the previously cached value or null if no entry existed or that entry value was evaluated to null
   * @throws NullPointerException if the provided key is null
   */
  public V remove(Serializable key) throws NullPointerException;

  /**
   * Performs a put in the cache.
   *
   * @param key the cache key
   * @param value the cached value
   * @throws NullPointerException if the key is null
   */
  public void put(K key, V value) throws NullPointerException;

  /**
   * Performs a put of all the entries provided by the map argument.
   *
   * @param objs the objects to put
   * @throws NullPointerException if the provided argument is null
   * @throws IllegalArgumentException if the provided map contains a null key
   */
  public void putMap(Map<? extends K, ? extends V> objs) throws NullPointerException, IllegalArgumentException;

  /**
   * Clears the cache.
   */
  public void clearCache();

  /**
   * Selects a subset of the cache.
   *
   * @param selector the selector
   * @throws Exception any exception
   */
  public void select(CachedObjectSelector<? super K, ? super V> selector) throws Exception;

  /**
   * Returns the number of entries in the cache.
   *
   * @return the size of the cache
   */
  public int getCacheSize();

  /**
   * Returns the maximum capacity of the cache.
   *
   * @return the maximum capacity
   */
  public int getMaxSize();

  /**
   * Sets the maximum capacity of the cache.
   *
   * @param max the maximum capacity
   */
  public void setMaxSize(int max);

  /**
   * Returns the maximum life time of an entry in the cache. The life time is a value in seconds, a negative
   * value means that the life time is infinite.
   *
   * @return the live time
   */
  public long getLiveTime();

  /**
   * Sets the maximum life time of an entry in the cache.
   *
   * @param period the live time
   */
  public void setLiveTime(long period);

  /**
   * Returns the number of time the cache was queried and a valid entry was returned.
   *
   * @return the cache hits
   */
  public int getCacheHit();

  /**
   * Returns the number of time the cache was queried and no entry was returned.
   *
   * @return the cache misses
   */
  public int getCacheMiss();

  /**
   * Returns a list of cached object that are considered as valid when the method is called. Any non valid
   * object will not be returnted.
   *
   * @return the list of cached objects
   * @throws Exception any exception
   */
  public List<? extends V> getCachedObjects() throws Exception;

  /**
   * Clears the cache and returns the list of cached object that are considered as valid when the method is called.
   * Any non valid
   * object will not be returned.
   *
   * @return the list of cached objects
   * @throws Exception any exception
   */
  public List<? extends V> removeCachedObjects();

  /**
   * Add a listener.
   *
   * @param listener the listener to add
   * @throws NullPointerException if the listener is null
   */
  public void addCacheListener(CacheListener<? super K, ? super V> listener) throws NullPointerException;

  public boolean isLogEnabled();

  public void setLogEnabled(boolean b);
}
