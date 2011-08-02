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
package org.etk.kernel.management.annotations;


/**
 * The type of impact of a managed method.
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 28, 2011  
 */
public enum ImpactType {

  /**
   * Read operation, does not affect state the managed resource.
   * Equivalent of {@link javax.management.MBeanOperationInfo#INFO}} 
   * for JMX and GET method for Rest.
   * 
   * 
   */
  READ,
  /**
   * Write operation, changes the state of the managed resource.
   * Equivalent of {@link javax.management.MBeanOperationInfo#INFO}}
   * for JMX and GET method for Rest.
   */
  WRITE,
  /**
   * Write operation, changes the state of the managed resource in an idempotent manner.
   * Equivalent of {@link javax.management.MBeanOperationInfo#INFO} for JMX and PUT method for Rest.
   */
  IDEMPOTENT_WRITE
  
  
}
