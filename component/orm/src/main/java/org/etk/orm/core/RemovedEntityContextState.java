package org.etk.orm.core;

import java.util.List;

import javax.jcr.Node;

import org.etk.orm.api.Status;
import org.etk.orm.plugins.jcr.NodeTypeInfo;
import org.etk.orm.plugins.jcr.type.PrimaryTypeInfo;
import org.etk.orm.plugins.vt2.ValueDefinition;

class RemovedEntityContextState extends EntityContextState {

  /** . */
  private final DomainSession session;

  /** . */
  private final String path;

  /** . */
  private final String name;

  /** . */
  private final PrimaryTypeInfo typeInfo;

  RemovedEntityContextState(DomainSession session, String path, String name, PrimaryTypeInfo typeInfo) {
    this.session = session;
    this.path = path;
    this.name = name;
    this.typeInfo = typeInfo;
  }

  String getLocalName() {
    return name;
  }

  String getPath() {
    throw new IllegalStateException();
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
    return Status.REMOVED;
  }

  PrimaryTypeInfo getTypeInfo() {
    return typeInfo;
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
    return "ObjectStatus[path=" + path + ",status=" + Status.REMOVED + "]";
  }
}
