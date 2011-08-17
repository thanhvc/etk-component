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
package org.etk.sandbox.orm;

import junit.framework.TestCase;

import org.etk.sandbox.orm.builder.ETKBuilder;
import org.etk.sandbox.orm.builder.ETKBuilderImpl;
import org.etk.sandbox.orm.core.ETKSession;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Aug 17, 2011  
 */
public abstract class BaseTestCase extends TestCase {

  protected ETKSession session;
  protected ETKBuilder builder;
  
  
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    builder = new ETKBuilderImpl();
    
    createDomain();
    
    builder.build();
    session = builder.boot();
  }
  
  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
  }
  
  public abstract void createDomain();
}
