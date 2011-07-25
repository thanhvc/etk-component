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
package org.etk.kernel.cache;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Feb 20, 2005
 * @version $Id: ExoCacheConfig.java 5799 2006-05-28 17:55:42Z geaz $
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
