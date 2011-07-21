package org.etk.orm.plugins.mapper;

import java.util.AbstractSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class SetMap<K, V> {

  /** . */
  private final Map<K, SetImpl> map = new HashMap<K, SetImpl>();

  public Set<V> peek(K key) {
    if (key == null) {
      throw new NullPointerException();
    }
    return map.get(key);
  }
  
  public Set<V> get(K key) {
    if (key == null) {
      throw new NullPointerException();
    }
    Set<V> set = map.get(key);
    if (set == null) {
      set = new SetImpl(key);
    }
    return set;
  }

  public Set<K> keySet() {
    return map.keySet();
  }
  
  private class SetImpl extends AbstractSet<V> {

    /** . */
    private final K key;
    
    /** . */
    private HashSet<V> set;

    /** . */
    private boolean added;

    private SetImpl(K key) {
      this.key = key;
      this.set = new HashSet<V>();
      this.added = false;
    }

    @Override
    public boolean add(V e) {
      if (!added) {
        if (map.containsKey(key)) {
          throw new IllegalStateException();
        } else {
          map.put(key, this);
          added = true;
        }
      }
      return set.add(e);
    }

    public Iterator<V> iterator() {
      return new Iterator<V>() {
        final Iterator<V> iterator = set.iterator();
        public boolean hasNext() {
          return iterator.hasNext();
        }
        public V next() {
          return iterator.next();
        }
        public void remove() {
          iterator.remove();
          if (set.size() == 0) {
            if (map.containsKey(key)) {
              map.remove(key);
            } else {
              throw new IllegalStateException();
            }
          }
        }
      };
    }

    public int size() {
      return set.size();
    }
  }

  @Override
  public String toString() {
    return map.toString();
  }
}
