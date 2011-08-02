package org.etk.core.rest.impl.header;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Produce sorted by quality value list of 'accept' header. In first it used for
 * parsing 'accept' and 'accept-language' headers.
 * 
 * @param <T> type that implements {@link QualityValue}
 */
public abstract class ListHeaderProducer<T extends QualityValue> {

  /**
   * Create each element of header list.
   * 
   * @param part the part of source string, it is part between two commas
   * @return newly created element of list
   */
  protected abstract T create(String part);

  /**
   * Create list of headers which is sorted by quality value. It is useful for
   * parsing 'accept' headers. If source list is null then empty list will be
   * returned.
   * 
   * @param header source header string
   * @return List of parsed sorted by quality value
   */
  public List<T> createQualitySortedList(String header) {
    List<T> l = new ArrayList<T>();

    int p = 0;
    int n = 0;
    while (p < header.length()) {

      n = header.indexOf(',', p);

      String token;
      if (n < 0) {
        token = header.substring(p);
        n = header.length();
      } else 
        token = header.substring(p, n);
      
      l.add(create(token));

      p = n + 1;
    }

    Collections.sort(l, HeaderHelper.QUALITY_VALUE_COMPARATOR);

    return l;
  }

}
