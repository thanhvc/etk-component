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
package org.etk.component.base.transaction;

import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          thanhvucong.78@google.com
 * Aug 6, 2011  
 */
public interface ExoResource {

  /**
   * @return XAResource
   */
  XAResource getXAResource();

  /**
   * Enlists XAResource in TM.
   * 
   * @throws javax.transaction.xa.XAException
   */
  void enlistResource() throws XAException;

  /**
   * Delists XAResource in TM.
   * 
   * @throws XAException
   */
  void delistResource() throws XAException;

  /**
   * Returns the payload attached to the resource.
   * 
   * @return the payload
   */
  Object getPayload();

  /**
   * Attach a payload to the resource.
   * 
   * @param payload the payload
   */
  void setPayload(Object payload);

}
