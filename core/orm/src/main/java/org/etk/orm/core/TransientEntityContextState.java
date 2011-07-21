package org.etk.orm.core;

import java.util.List;

import javax.jcr.Node;

import org.etk.orm.api.Status;
import org.etk.orm.plugins.jcr.NodeTypeInfo;
import org.etk.orm.plugins.jcr.type.PrimaryTypeInfo;
import org.etk.orm.plugins.vt2.ValueDefinition;

class TransientEntityContextState extends EntityContextState {

  /** . */
  private String localName;

  /** . */
  private final DomainSession session;

  TransientEntityContextState(DomainSession session) {
    this.session = session;
  }

  public String getLocalName() {
    return localName;
  }

  void setLocalName(String name) {
    this.localName = name;
  }

  String getPath() {
    return null;
  }

  String getId() {
    throw new IllegalStateException();
  }

  Node getNode() {
    throw new IllegalStateException();
  }

  DomainSession getSession() {
    return session;
  }

  Status getStatus() {
    return Status.TRANSIENT;
  }

  PrimaryTypeInfo getTypeInfo() {
    return null;
  }

  <V> V getPropertyValue(NodeTypeInfo nodeTypeInfo, String propertyName, ValueDefinition<?, V> vt) {
    throw new IllegalStateException();
  }

  <V> List<V> getPropertyValues(NodeTypeInfo nodeTypeInfo, String propertyName, ValueDefinition<?, V> vt, ListType listType) {
    throw new IllegalStateException();
  }

  <V> void setPropertyValue(NodeTypeInfo nodeTypeInfo, String propertyName, ValueDefinition<?, V> vt, V o) {
    throw new IllegalStateException();
  }

  <V> void setPropertyValues(NodeTypeInfo nodeTypeInfo, String propertyName, ValueDefinition<?, V> vt, ListType listType, List<V> objects) {
    throw new IllegalStateException();
  }

  public String toString() {
    return "ObjectStatus[status=" + Status.TRANSIENT + "]";
  }
}
