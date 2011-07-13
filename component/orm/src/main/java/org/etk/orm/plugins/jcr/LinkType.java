package org.etk.orm.plugins.jcr;

public enum LinkType {

  REFERENCE(0), PATH(1);

  final int index;

  LinkType(int index) {
    this.index = index;
  }
}
