/*
 * Copyright (C) 2003-2011 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.etk.orm.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>A package annotation that triggers node type generation for any Chromattic classes found in the annotated
 * package. The node types are generated in a resource named by default <code>nodetypes.xml</code> in the annotated
 * package. This resource should be available later at runtimpe via the {@link ClassLoader#getResource(String)}
 * mechanism.</p>
 *
 * <p>Class inclusion can be controlled and extended to sub packages with the {@link #deep()} parameter.</p>
 *
 * <p>Namespace can be declared and mapped to qualified name prefix to declare namespacing of the node types. For that
 * matter the {#namespaces} parameter can be used.</p>
 *
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.PACKAGE)
public @interface NodeTypeDefs {

  /**
   * Controls the inclusion of node types from sub packages of the annotated package.</p>
   *
   * @return true if the sub packages should be included
   */
  boolean deep() default false;

  /**
   * Defines the set of namespaces mapped in this node type definition declaration.
   *
   * @return the namespace mappings
   */
  NamespaceMapping[] namespaces() default {};

}

