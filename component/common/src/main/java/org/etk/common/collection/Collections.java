package org.etk.common.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

public class Collections {

  public static <K, V> V putIfAbsent(ConcurrentMap<K, V> map, K key, V value) {
    V previous = map.putIfAbsent(key, value);
    if (previous == null) { return value; } else { return previous; }
  }

  public static <E> List<E> list(Iterator<E> i) {
    LinkedList<E> list = new LinkedList<E>();
    while (i.hasNext()) {
      list.add(i.next());
    }
    return list;
  }

  public static <E> List<E> list(E... elements) {
    return new ArrayList<E>(Arrays.asList(elements));
  }

  public static <E> HashSet<E> set(Iterator<E> i) {
    HashSet<E> set = new HashSet<E>();
    while (i.hasNext()) {
      set.add(i.next());
    }
    return set;
  }

  public static <E> HashSet<E> set(Iterable<E> i) {
    return set(i.iterator());
  }

  public static <E> HashSet<E> set(E... es) {
    HashSet<E> set = new HashSet<E>();
    for (E e : es) {
      set.add(e);
    }
    return set;
  }

}

