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
package org.etk.kernel.cache.api;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 25, 2011  
 */
public class ExoCacheConfig {
  private String  name;

  private String  label;

  private int     maxSize;

  private long    liveTime;

  private boolean distributed = false;

  private boolean replicated  = false;

  private String  implementation;
  
  private boolean logEnabled = false;

  public String getName() {
    return name;
  }

  public void setName(String s) {
    name = s;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String s) {
    label = s;
  }

  public int getMaxSize() {
    return maxSize;
  }

  public void setMaxSize(int size) {
    maxSize = size;
  }

  public long getLiveTime() {
    return liveTime;
  }

  public void setLiveTime(long period) {
    liveTime = period * 1000;
  }

  public boolean isDistributed() {
    return distributed;
  }

  public void setDistributed(boolean b) {
    distributed = b;
  }

  // public void setDistributed(String b) { distributed_ = "true".equals(b) ; }

  public boolean isRepicated() {
    return replicated;
  }

  public void setReplicated(boolean b) {
    replicated = b;
  }

  public String getImplementation() {
    return implementation;
  }

  public void setImplementation(String alg) {
    implementation = alg;
  }

  public boolean isLogEnabled() {
    return logEnabled;
  }

  public void setLogEnabled(boolean enableLogging) {
    this.logEnabled = enableLogging;
  }
}