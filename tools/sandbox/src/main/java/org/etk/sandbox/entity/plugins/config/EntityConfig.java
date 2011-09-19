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
package org.etk.sandbox.entity.plugins.config;

import java.util.concurrent.atomic.AtomicReference;

import org.etk.kernel.container.configuration.ConfigurationException;
import org.etk.kernel.container.xml.InitParams;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Sep 19, 2011  
 */
public class EntityConfig {
  
  private static volatile AtomicReference<EntityConfig> configRef = new AtomicReference<EntityConfig>();

  // ========== engine info fields ==========
  private final String txFactoryClass = null;
  private final String txFactoryUserTxJndiName = null;
  private final String txFactoryUserTxJndiServerName = null;
  private final String txFactoryTxMgrJndiName = null;
  private final String txFactoryTxMgrJndiServerName = null;
  private final String connFactoryClass = null;

  public EntityConfig(InitParams params) throws ConfigurationException {
    if (params == null) {
      throw new ConfigurationException("Initializations parameters expected");
    }
  }
  
}
