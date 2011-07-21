package org.etk.orm.plugins.jcr;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.etk.orm.api.UndeclaredRepositoryException;


public class NodeSet extends AbstractSet<Node> {

  /** . */
  private HashMap<String, Node> map = null;

  public boolean contains(Object o) {
    if (map != null && o instanceof Node) {
      try {
        return map.containsKey(((Node)o).getUUID());
      }
      catch (RepositoryException e) {
        throw new UndeclaredRepositoryException(e);
      }
    }
    return false;
  }

  public Iterator<Node> iterator() {
    return map != null ? map.values().iterator() : Collections.<Node>emptyList().iterator();
  }

  public Object[] toArray() {
    return map.values().toArray();
  }

  public <T> T[] toArray(T[] a) {
    return map.values().toArray(a);
  }

  public boolean add(Node node) {
    if (map == null) {
      map = new HashMap<String, Node>();
    }
    try {
      return map.put(node.getUUID(), node) != null;
    }
    catch (RepositoryException e) {
      throw new UndeclaredRepositoryException(e);
    }
  }

  @Override
  public int size() {
    return map.size();
  }

  public boolean containsAll(Collection<?> c) {
    for (Object o : c) {
      if (!contains(o)) {
        return false;
      }
    }
    return true;
  }

  public boolean addAll(Collection<? extends Node> c) {
    boolean changed = false;
    for (Object o : c) {
      if (o instanceof Node) {
        changed |= add((Node)o);
      }
    }
    return changed;
  }

  public boolean retainAll(Collection<?> c) {
    if (map == null) {
      return false;
    }
    Set<String> keys = keys(c);
    return map.keySet().retainAll(keys);
  }

  public boolean removeAll(Collection<?> c) {
    if (map == null) {
      return false;
    }
    Set<String> keys = keys(c);
    return map.keySet().removeAll(keys);
  }

  private Set<String> keys(Collection<?> c) {
    Set<String> keys = new HashSet<String>();
    for (Object o : c) {
      if (o instanceof Node) {
        try {
          keys.add(((Node)o).getUUID());
        }
        catch (RepositoryException e) {
          throw new UndeclaredRepositoryException(e);
        }
      }
    }
    return keys;
  }
}
