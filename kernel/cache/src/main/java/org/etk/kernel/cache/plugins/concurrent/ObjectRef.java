package org.etk.kernel.cache.plugins.concurrent;

import java.io.Serializable;

import org.etk.kernel.cache.api.ObjectCacheInfo;

/**
 * An reference to an object.
 * 
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 25, 2011  
 */
abstract class ObjectRef<K extends Serializable, V> extends Item implements ObjectCacheInfo<V> {

  protected final long expirationTime;
  protected final K name;

  protected ObjectRef(long expirationTime, K name) {
    this.name = name;
    this.expirationTime = expirationTime;
  }

  public abstract boolean isValid();

  public abstract V getObject();

 
  public long getExpireTime() {
    return expirationTime;
  }

  public V get() {
    return getObject();
  }
}
