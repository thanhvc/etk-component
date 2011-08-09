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

package org.exoplatform.services.security;

import java.util.concurrent.ConcurrentHashMap;

import org.exoplatform.services.log.Log;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.services.log.ExoLogger;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author Gennady Azarenkov
 * @version $Id: $
 */

public class IdentityRegistry {

  /**
   * "concurrency-level".
   */
  public static final String                        INIT_PARAM_CONCURRENCY_LEVEL = "concurrency-level";

  /**
   * Logger.
   */
  private static final Log                          LOG                          = ExoLogger.getLogger(IdentityRegistry.class.getName());

  /**
   * Default concurrency level.
   */
  private static final int                           DEFAULT_CONCURRENCY_LEVEL    = 16;

  /**
   * Identities.
   */
  private final ConcurrentHashMap<String, Identity> identities;

  public IdentityRegistry(InitParams params) {
    this(parseConcurrencyLevel(params));
  }

  /**
   * Try to parse concurrency level attribute from init parameters.
   * 
   * @param params See {@link InitParams}
   * @return parsed parameter or default if parameter not set or can't be parsed
   */
  private static int parseConcurrencyLevel(InitParams params) {
    try {
      return Integer.valueOf(params.getValueParam(INIT_PARAM_CONCURRENCY_LEVEL).getValue());
    } catch (NullPointerException e) {
      LOG.warn("Parameter " + INIT_PARAM_CONCURRENCY_LEVEL
          + " was not found in configuration, default " + DEFAULT_CONCURRENCY_LEVEL + "will be used.");
      return DEFAULT_CONCURRENCY_LEVEL;
    } catch (Exception e) {
      LOG.error("Can't parse parameter " + INIT_PARAM_CONCURRENCY_LEVEL, e);
      return DEFAULT_CONCURRENCY_LEVEL;
    }
  }

  /**
   * Create identity registry.
   * @param concurrencyLevel concurrency level for ConcurrentHashMap 
   */
  private IdentityRegistry(int concurrencyLevel) {
    identities = new ConcurrentHashMap<String, Identity>(concurrencyLevel, 0.75f, concurrencyLevel);
  }

  /**
   * Get identity for supplied user ID.
   * @param userId user ID
   * @return identity or null if not found
   */
  public Identity getIdentity(String userId) {
    return identities.get(userId);
  }

  /**
   * Register new identity in registry.
   * @param identity {@link Identity}
   */
  public void register(Identity identity) {
    this.identities.put(identity.getUserId(), identity);
  }

  /**
   * Remove identity with supplied user ID. 
   * @param userId user ID
   */
  public void unregister(String userId) {
    this.identities.remove(userId);
  }

  /**
   * Remove all identities.
   */
  void clear() {
    identities.clear();
  }

}
