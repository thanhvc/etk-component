/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.services.cache.concurrent;

import java.util.ArrayList;

/**
 * The queue needed by the concurrent FIFO cache.
 *
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public interface Queue<I extends Item> {

  /**
   * Returns the queue size
   * @return the size
   */
  public int size();

  /**
   * Attempt to remove an item from the queue.
   *
   * @param item the item to remove
   * @return true if the item was removed by this thread
   */
  public boolean remove(I item);

  /**
   * Add the item to the head of the list.
   *
   * @param item the item to add
   */
  public void add(I item);

  /**
   * Attempt to trim the queue. Trim will occur if no other thread is already performing a trim
   * and the queue size is greater than the provided size.
   *
   * @param size the wanted size
   * @return the list of evicted items
   */
  public ArrayList<I> trim(int size);
}


