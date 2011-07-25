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
package org.etk.kernel.cache.plugins;

import java.io.Serializable;



/**
 * The cache listener allows to be aware of various events that occurs for a cache. For performance reason
 * a listener must perform short time lived operations or should consider to perform jobs asynchronously.
 *
 */
public interface CacheListener<K extends Serializable, V> {

  /**
   * An entry is expired from the cache.
   *
   * @param context the listener context
   * @param key the entry key
   * @param obj the entry value
   * @throws Exception any exception
   */
  public void onExpire(CacheListenerContext context, K key, V obj) throws Exception;

  /**
   * An entry is removed from the cache.
   *
   * @param context the listener context
   * @param key the entry key
   * @param obj the entry value
   * @throws Exception any exception
   */
  public void onRemove(CacheListenerContext context, K key, V obj) throws Exception;

  /**
   * An entry is inserted in the cache.
   *
   * @param context the listener context
   * @param key the entry key
   * @param obj the entry value
   * @throws Exception any exception
   */
  public void onPut(CacheListenerContext context, K key, V obj) throws Exception;

  /**
   * An entry is retrieved from the cache.
   *
   * @param context the listener context
   * @param key the entry key
   * @param obj the entry value
   * @throws Exception any exception
   */
  public void onGet(CacheListenerContext context, K key, V obj) throws Exception;

  /**
   * The cache is globally cleared.
   *
   * @param context the listener context
   * @throws Exception any exception
   */
  public void onClearCache(CacheListenerContext context) throws Exception;
}