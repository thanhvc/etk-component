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
package org.etk.service.bar.impl;

import java.util.List;

import org.etk.service.bar.BarFilter;
import org.etk.service.bar.BarLifeCycle;
import org.etk.service.bar.BarListenerPlugin;
import org.etk.service.bar.api.BarService;
import org.etk.service.foo.FooFilter;
import org.etk.service.foo.FooLifeCycle;
import org.etk.service.foo.FooListenerPlugin;
import org.etk.service.foo.MockRepositoryService;
import org.etk.service.foo.model.Bar;
import org.etk.service.foo.model.Foo;
import org.etk.storage.api.BarStorage;
import org.etk.storage.api.FooStorage;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 21, 2011  
 */
public class BarServiceImpl implements BarService {

  private BarStorage barStorage;
  private BarLifeCycle barLifeCycle = new BarLifeCycle();
  
  public BarServiceImpl(BarStorage barStorage) {
    this.barStorage = barStorage;
  }
  
  @Override
  public Bar getBarByDescription(String description) {
    return null;
  }

  @Override
  public Bar getBarById(String barId) {
    return barStorage.findById(barId);
  }

  @Override
  public List<Bar> getAllBarsWithListAccess() {
    return null;
  }

  @Override
  public List<Bar> getAllBarsByFilter(BarFilter filter) {
    return barStorage.getBarByFilter(filter, 0, 5);
  }

  @Override
  public Bar createBar(Bar bar) {
    barStorage.saveBar(bar);
    barLifeCycle.barCreated(bar, "executor");
    return bar;
  }

  @Override
  public Bar updateBar(Bar existingBar) {
    barStorage.saveBar(existingBar);
    barLifeCycle.barUpdated(existingBar, "executor");
    return existingBar;
  }

  @Override
  public void deleteBar(Bar existingBar) {
    barStorage.deleteBar(existingBar);
    barLifeCycle.barDeleted(existingBar, "executor");
  }

  @Override
  public void registerBarListenerPlugin(BarListenerPlugin barListenerPlugin) {
    barLifeCycle.addListener(barListenerPlugin);
    
  }

  @Override
  public void unregisterListenerPlugin(BarListenerPlugin barListenerPlugin) {
    barLifeCycle.removeListener(barListenerPlugin);
  }
  
  public void addBarListener(BarListenerPlugin plugin) {
    registerBarListenerPlugin(plugin);
  }

}
