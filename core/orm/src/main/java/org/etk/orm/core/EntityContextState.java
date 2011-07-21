package org.etk.orm.core;

import java.util.List;

import javax.jcr.Node;

import org.etk.orm.api.Status;
import org.etk.orm.plugins.jcr.NodeTypeInfo;
import org.etk.orm.plugins.jcr.type.PrimaryTypeInfo;
import org.etk.orm.plugins.vt2.ValueDefinition;


abstract class EntityContextState {

  abstract String getId();

  abstract String getLocalName();

  abstract String getPath();

  abstract Node getNode();

  abstract DomainSession getSession();

  abstract Status getStatus();

  abstract PrimaryTypeInfo getTypeInfo();

  abstract <V> V getPropertyValue(NodeTypeInfo nodeTypeInfo, String propertyName, ValueDefinition<?, V> vt);

  abstract <V> List<V> getPropertyValues(NodeTypeInfo nodeTypeInfo, String propertyName, ValueDefinition<?, V> vt, ListType listType);

  abstract <V> void setPropertyValue(NodeTypeInfo nodeTypeInfo, String propertyName, ValueDefinition<?, V> vt, V o);

  abstract <V> void setPropertyValues(NodeTypeInfo nodeTypeInfo, String propertyName, ValueDefinition<?, V> vt, ListType listType, List<V> objects);

}