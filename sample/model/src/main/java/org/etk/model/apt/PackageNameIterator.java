package org.etk.model.apt;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.etk.model.apt.PackageNameIterator;

class PackageNameIterator implements Iterator<String> {

  public static Iterable<String> with(final String packageName) {
    if (packageName == null) {
      throw new NullPointerException();
    }
    return new Iterable<String>() {
      public Iterator<String> iterator() {
        return new PackageNameIterator(packageName);
      }
    };
  }

  /** . */
  private String packageName;

  PackageNameIterator(String packageName) {
    if (packageName == null) {
      throw new NullPointerException();
    }
    this.packageName = packageName;
  }

  public boolean hasNext() {
    return packageName.lastIndexOf('.') != -1;
  }

  public String next() {
    String next = packageName;
    int pos = packageName.lastIndexOf('.');
    if (pos == -1) {
      throw new NoSuchElementException();
    }
    packageName = packageName.substring(0, pos);
    return next;
  }

  public void remove() {
    throw new UnsupportedOperationException();
  }
}

