/*
 * Copyright (C) 2003-2008 eXo Platform SAS.
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
package org.etk.kernel.cache.impl;

import java.io.Serializable;

import org.etk.common.logging.Logger;
import org.etk.kernel.cache.CacheListener;
import org.etk.kernel.cache.CacheListenerContext;


/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Oct 4, 2008  
 */
public class LoggingCacheListener implements CacheListener {
  
  Logger log = Logger.getLogger(LoggingCacheListener.class);

  public void onClearCache(CacheListenerContext context) throws Exception {
      if (log.isDebugEnabled()) {
        log.debug("Cleared region " + context.getCacheInfo().getName());
      }
  }

  public void onExpire(CacheListenerContext context, Serializable key, Object obj) throws Exception {
    if (log.isTraceEnabled()) {
      log.trace("Expired entry " + key + " on region " + context.getCacheInfo().getName());
    }
  }

  public void onGet(CacheListenerContext context, Serializable key, Object obj) throws Exception {
    if (log.isTraceEnabled()) {
      log.trace("Get entry " + key + " on region " + context.getCacheInfo().getName());
    }
  }

  public void onPut(CacheListenerContext context, Serializable key, Object obj) throws Exception {
    if (log.isTraceEnabled()) {
      log.trace("Put entry " + key + " region " + context.getCacheInfo().getName());
    }
    if (log.isWarnEnabled()) {
      int maxSize = context.getCacheInfo().getMaxSize();
      int size = context.getCacheInfo().getSize();
      double treshold = maxSize*0.95;
      if (size >= treshold) {
        log.warn("region " + context.getCacheInfo().getName() + " is 95% full, consider extending maxSize");
      }
    }
    
  }

  public void onRemove(CacheListenerContext context, Serializable key, Object obj) throws Exception {
    if (log.isTraceEnabled()) {
      log.trace("Removed entry " + key + " region " + context.getCacheInfo().getName());
    }
  }
}
