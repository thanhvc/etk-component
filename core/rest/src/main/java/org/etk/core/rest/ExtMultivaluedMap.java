package org.etk.core.rest;

import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

/**
 * Extension of {@link MultivaluedMap} that allows to get not null value (empty
 * list) even there is no mapping value to supplied key.
 * 
 * @param <K> key
 * @param <V> value
 * @see #getList(Object)
 */
public interface ExtMultivaluedMap<K, V> extends MultivaluedMap<K, V> {

  /**
   * @param key key
   * @return never null even any value not found in the map, return empty list
   *         instead
   */
  List<V> getList(K key);

}