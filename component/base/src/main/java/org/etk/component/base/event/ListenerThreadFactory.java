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
package org.etk.component.base.event;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          thanhvucong.78@google.com
 * Aug 6, 2011  
 */
public class ListenerThreadFactory implements ThreadFactory
{
   static final AtomicInteger poolNumber = new AtomicInteger(1);

   final ThreadGroup group;

   final AtomicInteger threadNumber = new AtomicInteger(1);

   final String namePrefix;

   ListenerThreadFactory()
   {
      SecurityManager s = System.getSecurityManager();
      group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
      namePrefix = "asynch-event-" + poolNumber.getAndIncrement() + "-thread-";
   }

   /**
    * {@inheritDoc}
    */
   public Thread newThread(Runnable r)
   {
      Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
      if (t.isDaemon())
         t.setDaemon(false);
      if (t.getPriority() != Thread.NORM_PRIORITY)
         t.setPriority(Thread.NORM_PRIORITY);
      return t;
   }
}
